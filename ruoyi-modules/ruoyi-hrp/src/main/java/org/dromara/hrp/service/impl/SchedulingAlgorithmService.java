package org.dromara.hrp.service.impl;

import cn.hutool.core.convert.impl.MapConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.hrp.domain.HrpAttendanceExceptions;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.domain.HrpStores;
import org.dromara.hrp.domain.dto.FeedbackItem;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.vo.*;
import org.dromara.hrp.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能排班算法核心服务 (已重构和修复)
 * @author Gemini AI Assistant
 */
@Service
public class SchedulingAlgorithmService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingAlgorithmService.class);

    // --- 算法可配置参数 (未来应移至数据库或配置中心) ---
    private static final int MAX_CONSECUTIVE_WORK_DAYS = 6;     // 最大连续工作天数
    private static final double MAX_WEEKLY_HOURS = 40.0;        // 计时员工每周最大工时
    private static final int MAX_FULLTIME_LEAVE_PER_DAY = 2;    // 每日最多允许的全职休假人数
    private static final int FULL_TIME_MONTHLY_REST_DAYS = 8;   // 全职员工每月最少休息天数
    private static final long BREAK_COVERAGE_THRESHOLD_MINUTES = 90; // 超过此分钟数的休息需要安排替班

    // --- 依赖注入 ---
    @Autowired private HrpUserProfileMapper userProfileMapper;
    @Autowired private HrpUserSkillsMapper userSkillsMapper;
    @Autowired private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Autowired private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Autowired private HrpScheduleRequirementsMapper scheduleRequirementsMapper;
    @Autowired private HrpShiftsMapper shiftsMapper;
    @Autowired private HrpSchedulesMapper schedulesMapper;
    @Autowired private HrpStoreEventsMapper storeEventsMapper;
    @Autowired private HrpSkillsMapper skillsMapper;
    @Autowired private HrpStoresMapper storesMapper;
    @Autowired private HrpShiftBreaksMapper shiftBreaksMapper;

    /**
     * 智能排班算法主入口
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleGenerationResult generateSchedule(ScheduleGenerateDto dto) {
        List<FeedbackItem> feedbackItems = new ArrayList<>();
        log.info("智能排班任务启动 V2.1，分店ID: {}, 日期范围: {} to {}", dto.getStoreId(), dto.getStartDate(), dto.getEndDate());

        // 1. 准备算法输入数据
        AlgorithmInput input = prepareInputData(dto, feedbackItems);
        if (!feedbackItems.isEmpty() && CollectionUtils.isEmpty(input.getRequirements())) {
            log.error("数据准备阶段发生严重错误，排班中止。");
            return new ScheduleGenerationResult(new ArrayList<>(), feedbackItems);
        }

        // 2. 初始化排班矩阵并处理休假
        ScheduleMatrix matrix = initializeMatrix(input, dto, feedbackItems);

        // 3. 【核心步骤一】优先安排全职员工
        assignFullTimeEmployees(matrix, input, dto.getSchedulingMode());

        // 4. 【核心步骤二】生成并安排休息时段的替班需求
        List<BreakCoverageRequirement> breakRequirements = generateBreakCoverageRequirements(matrix, input);
        fillBreakCoverageShifts(breakRequirements, matrix, input, dto.getSchedulingMode());

        // 5. 【核心步骤三】填补常规班次的剩余缺口
        fillRemainingShifts(matrix, input, dto.getSchedulingMode());

        // 6. 【核心步骤四】为全职员工补足休息日并最终检查
        finalizeFullTimeSchedules(matrix, input, feedbackItems);

        // 7. 生成并保存结果
        List<HrpSchedules> generatedSchedules = buildAndSaveResults(matrix, dto);

        // 8. 生成结构化的缺口报告
        generateUnsatisfiedRequirementsReport(matrix, input, breakRequirements, feedbackItems);

        log.info("智能排班任务完成，共生成 {} 条排班记录。", generatedSchedules.size());
        return new ScheduleGenerationResult(generatedSchedules, feedbackItems);
    }

    /**
     * 步骤 1: 准备所有需要的数据
     * @param dto 前端请求
     * @param feedbackItems 用于收集结构化警告和错误信息的列表
     * @return 算法所需的所有输入数据集合
     */
    private AlgorithmInput prepareInputData(ScheduleGenerateDto dto, List<FeedbackItem> feedbackItems) {
        Long storeId = dto.getStoreId();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        // 获取本店设定的跨日工时归属规则
        HrpStores store = storesMapper.selectById(storeId);
        if (store == null) throw new ServiceException("找不到指定的分店信息");
        String crossDayRule = store.getCrossDayRule();

        // 获取参与排班的员工ID
        List<Long> userIds = dto.getEmployees().stream()
            .map(ScheduleGenerateDto.EmployeeConfig::getId)
            .collect(Collectors.toList());
        if (userIds.isEmpty()) throw new ServiceException("请选择至少一名员工参与排班");

        // 查询员工档案、技能、可用时间、请假记录、班别、休息时间、人力需求和特殊事件
        List<HrpUserProfileVo> employees = userProfileMapper.selectVoByUserIds(userIds);
        if (employees.isEmpty()) throw new ServiceException("未找到任何有效的员工信息");

        Map<Long, List<Long>> userSkills = userSkillsMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserSkillsVo::getUserId, Collectors.mapping(HrpUserSkillsVo::getSkillId, Collectors.toList())));

        Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities = userAvailabilityMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserAvailabilityVo::getUserId));

        List<HrpLeaveRequestsVo> leaveRequests = leaveRequestsMapper.selectVoListByUsersAndDate(userIds, startDate, endDate);
        List<HrpShiftsVo> shifts = shiftsMapper.selectVoListByStoreId(storeId);
        // 注意：此处获取了所有休息时间记录，后续会按班次ID进行分组
        List<HrpShiftBreaksVo> breaks = shiftBreaksMapper.selectVoList();

        // 使用前端传入的动态需求，并将其转换为算法内部使用的数据结构
        List<HrpScheduleRequirementsVo> requirements = convertDtoToRequirements(dto.getRequirements(), shifts, feedbackItems);
        List<HrpStoreEventsVo> storeEvents = storeEventsMapper.selectVoListByStoreAndDate(storeId, startDate, endDate);

        return new AlgorithmInput(startDate, endDate, storeId, crossDayRule, employees, userSkills, userAvailabilities, leaveRequests, shifts, breaks, requirements, storeEvents);
    }

    /**
     * 辅助方法: 将DTO中的动态需求转换为算法所需的内部格式 (HrpScheduleRequirementsVo)
     * @param dtoRequirements DTO中的需求Map
     * @param allShifts 当前分店的所有班次定义
     * @param feedbackItems 用于收集配置错误的反馈列表
     * @return 转换后的需求列表
     */
    private List<HrpScheduleRequirementsVo> convertDtoToRequirements(Map<String, Map<String, Integer>> dtoRequirements, List<HrpShiftsVo> allShifts, List<FeedbackItem> feedbackItems) {
        List<HrpScheduleRequirementsVo> reqList = new ArrayList<>();

        // 预先加载所有技能信息，并创建 名称 -> ID 的映射，以提高查询效率
        List<HrpSkills> allSkills = skillsMapper.selectList();
        Map<String, Long> skillNameToIdMap = allSkills.stream()
            .collect(Collectors.toMap(HrpSkills::getName, HrpSkills::getId, (existing, replacement) -> existing));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 遍历前端传入的每一个时间段 (e.g., "10:30-18:00")
        dtoRequirements.forEach((timeSlot, skills) -> {
            // 根据时间段字符串查找匹配的班次ID
            Long shiftId = allShifts.stream()
                .filter(s -> {
                    String startTime = s.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter);
                    String endTime = s.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter);
                    return timeSlot.equals(startTime + "-" + endTime);
                })
                .map(HrpShiftsVo::getId)
                .findFirst()
                .orElse(null);

            // 如果找不到匹配的班次，记录一个配置错误
            if (shiftId == null) {
                String message = String.format("配置错误: 未找到与时间段 [%s] 完全匹配的班次定义。", timeSlot);
                log.warn(message);
                feedbackItems.add(FeedbackItem.builder()
                    .type(FeedbackItem.FeedbackType.SYSTEM_WARNING)
                    .severity(FeedbackItem.Severity.ERROR)
                    .message(message)
                    .build());
                return; // 跳过此时间段的需求处理
            }

            // 遍历该时间段下的每个岗位及其需求人数 (e.g., "出锅": 2)
            skills.forEach((skillName, count) -> {
                // 根据岗位名称查找ID
                Long skillId = skillNameToIdMap.get(skillName);
                // 如果找不到匹配的技能，记录一个配置错误
                if (skillId == null) {
                    String message = String.format("配置错误: 未找到名为 '%s' 的技能/岗位。", skillName);
                    log.warn(message);
                    feedbackItems.add(FeedbackItem.builder()
                        .type(FeedbackItem.FeedbackType.SYSTEM_WARNING)
                        .severity(FeedbackItem.Severity.ERROR)
                        .message(message)
                        .build());
                    return; // 跳过此岗位的需求处理
                }

                // 只有当需求人数大于0时，才创建需求记录
                if (count > 0) {
                    HrpScheduleRequirementsVo req = new HrpScheduleRequirementsVo();
                    req.setDayType("平日"); // 动态需求统一按平日处理
                    req.setShiftId(shiftId);
                    req.setSkillId(skillId);
                    req.setRequiredCount(count);
                    reqList.add(req);
                }
            });
        });
        return reqList;
    }


    /**
     * 步骤 2: 初始化排班矩阵, 处理休假申请与预检查
     * @param input 算法输入数据
     * @param dto 前端请求
     * @param feedbackItems 用于收集结构化警告和错误信息的列表
     * @return 配置完成的排班矩阵
     */
    private ScheduleMatrix initializeMatrix(AlgorithmInput input, ScheduleGenerateDto dto, List<FeedbackItem> feedbackItems) {
        ScheduleMatrix matrix = new ScheduleMatrix(input.getStartDate(), input.getEndDate(), input.getEmployees(), input.getShiftsById(), input.getShiftBreaksByShiftId());

        // 2.1 处理全职员工休假申请冲突
        handleFullTimeLeaveConflicts(input, feedbackItems);

        // 2.2 锁定所有已批准的休假日和前端指定的固定休息日
        for (HrpLeaveRequestsVo leave : input.getLeaveRequests()) {
            if ("已提交".equals(leave.getApprovalStatus()) || "已锁定".equals(leave.getApprovalStatus())) {
                matrix.blockDay(leave.getUserId(), leave.getLeaveDate(), "休假");
            }
        }
        for (ScheduleGenerateDto.EmployeeConfig empConfig : dto.getEmployees()) {
            if (empConfig.getDaysOff() != null) {
                for (String dayOffLabel : empConfig.getDaysOff()) {
                    LocalDate date = findDateByLabel(dayOffLabel, input.getStartDate(), input.getEndDate());
                    if (date != null) {
                        matrix.blockDay(empConfig.getId(), date, "休息");
                    }
                }
            }
        }

        // 2.3 (预检查) 检查全职员工的休假是否过多，导致无法满足最低工时
        long totalDaysInPeriod = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;
        input.getEmployees().stream()
            .filter(e -> "全职".equals(e.getEmployeeType()))
            .forEach(emp -> {
                long blockedDays = matrix.getBlockedDaysCount(emp.getUserId());
                long availableWorkDays = totalDaysInPeriod - blockedDays;

                // 【问题修复】: 此处修改为生成 FeedbackItem 对象
                if (availableWorkDays < (totalDaysInPeriod - FULL_TIME_MONTHLY_REST_DAYS)) {
                    String message = String.format("全职员工 [%s] 的预设休假过多(%d天)，可能无法满足月度最低工时要求。", emp.getUserName(), blockedDays);
                    feedbackItems.add(FeedbackItem.builder()
                        .type(FeedbackItem.FeedbackType.CONSTRAINT_VIOLATION)
                        .severity(FeedbackItem.Severity.WARNING)
                        .message(message)
                        .relatedUserIds(Collections.singletonList(emp.getUserId()))
                        .build());
                    log.warn(message);
                }
            });

        return matrix;
    }
    /**
     * 辅助方法: 根据标签（如 "星期一 (8.26)"）找到对应的日期
     */
    private LocalDate findDateByLabel(String label, LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String currentLabel = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.CHINESE) + " (" + date.getMonthValue() + "." + date.getDayOfMonth() + ")";
            if (label.equals(currentLabel)) {
                return date;
            }
        }
        return null; // or throw exception
    }


    /**
     * 辅助方法: 处理全职员工休假申请冲突。
     * 如果某一天申请休假的全职员工超过了系统设定的上限 (MAX_FULLTIME_LEAVE_PER_DAY)，
     * 则会自动进行随机抽签，并将超出部分的员工休假申请标记为“已取消”，同时生成通知。
     *
     * @param input 算法输入数据
     * @param feedbackItems 用于收集结构化警告和错误信息的列表
     */
    private void handleFullTimeLeaveConflicts(AlgorithmInput input, List<FeedbackItem> feedbackItems) {
        // 1. 筛选出所有由“全职”员工提交的“已提交”状态的休假申请，并按日期分组
        Map<LocalDate, List<HrpLeaveRequestsVo>> fullTimeLeaveMap = input.getLeaveRequests().stream()
            .filter(lr -> "全职".equals(findEmployeeType(lr.getUserId(), input.getEmployees())) && "已提交".equals(lr.getApprovalStatus()))
            .collect(Collectors.groupingBy(HrpLeaveRequestsVo::getLeaveDate));

        // 2. 遍历每一个有休假申请的日期
        fullTimeLeaveMap.forEach((date, requests) -> {
            // 3. 判断当天的申请人数是否超过了上限
            if (requests.size() > MAX_FULLTIME_LEAVE_PER_DAY) {
                log.warn("日期 [{}] 的全职员工休假申请人数 ({}) 超过上限 ({}), 进行抽签处理...", date, requests.size(), MAX_FULLTIME_LEAVE_PER_DAY);

                // 4. 进行随机抽签（打乱列表顺序）
                Collections.shuffle(requests);

                // 5. 遍历超出上限的休假申请
                for (int i = MAX_FULLTIME_LEAVE_PER_DAY; i < requests.size(); i++) {
                    HrpLeaveRequestsVo deniedRequest = requests.get(i);
                    // 将其状态在内存中更新为“已取消”，后续排班逻辑将不会再视其为休假日
                    deniedRequest.setApprovalStatus("已取消");

                    // 【问题修复】: 此处修改为生成 FeedbackItem 对象作为通知
                    String employeeName = findEmployeeName(deniedRequest.getUserId(), input.getEmployees());
                    String message = String.format("因超出每日全职休假上限，员工 [%s] 于 %s 的休假申请已被系统自动取消。", employeeName, date);

                    feedbackItems.add(FeedbackItem.builder()
                        .type(FeedbackItem.FeedbackType.SYSTEM_WARNING) // 类型为系统警告/通知
                        .severity(FeedbackItem.Severity.WARNING)      // 级别为警告
                        .message(message)
                        .date(date)                                     // 关联日期
                        .relatedUserIds(Collections.singletonList(deniedRequest.getUserId())) // 关联被取消的员工ID
                        .build());
                    log.info(message);
                }
            }
        });
    }

    /**
     * 步骤 3: 优先安排全职员工
     */
    private void assignFullTimeEmployees(ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        log.info("开始优先为全职员工排班...");
        List<HrpUserProfileVo> fullTimeEmployees = input.getEmployees().stream()
            .filter(e -> "全职".equals(e.getEmployeeType()))
            .collect(Collectors.toList());

        // TODO: 获取全职员工的月度目标工时，这里暂时使用一个估算值
        double monthlyHourTarget = 160.0;

        for (HrpUserProfileVo employee : fullTimeEmployees) {
            // 循环日期，为该员工寻找合适的班次，直到达到工时目标或没有合适的班次
            for (LocalDate currentDate = input.getStartDate(); !currentDate.isAfter(input.getEndDate()); currentDate = currentDate.plusDays(1)) {
                if (matrix.getTotalHours(employee.getUserId()) >= monthlyHourTarget) {
                    break; // 工时已满
                }
                if (!matrix.isAvailable(employee.getUserId(), currentDate)) {
                    continue; // 当天不可用
                }

                // 寻找当天最适合该员工的班次 (这里简化为寻找第一个可上的班次)
                findAndAssignBestShiftForEmployee(employee, currentDate, matrix, input, schedulingMode);
            }
        }
    }

    /**
     * 为单个员工在指定日期寻找并安排一个最合适的班次
     */
    private void findAndAssignBestShiftForEmployee(HrpUserProfileVo employee, LocalDate date, ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        // 筛选出当天该员工能上的所有班次
        List<HrpShiftsVo> possibleShifts = input.getShifts().stream()
            .filter(shift -> {
                // 检查技能是否匹配该班次的任何一个需求
                boolean hasMatchingSkill = input.getRequirements().stream()
                    .anyMatch(req -> req.getShiftId().equals(shift.getId()) &&
                        input.getUserSkills().getOrDefault(employee.getUserId(), Collections.emptyList()).contains(req.getSkillId()));

                return hasMatchingSkill &&
                    matrix.isAvailable(employee.getUserId(), date) &&
                    isAvailableForShift(employee.getUserId(), date, shift, input) &&
                    matrix.getConsecutiveWorkDays(employee.getUserId(), date) < MAX_CONSECUTIVE_WORK_DAYS;
            })
            .collect(Collectors.toList());

        if (possibleShifts.isEmpty()) return;

        // 排序找到最优班次，例如：优先满足最缺人的班次
        possibleShifts.sort(Comparator.comparingInt((HrpShiftsVo shift) ->
            input.getRequirements().stream()
                .filter(req -> req.getShiftId().equals(shift.getId()))
                .mapToInt(req -> req.getRequiredCount() - matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId()))
                .sum()
        ).reversed());

        HrpShiftsVo bestShift = possibleShifts.get(0);

        // 找到该班次中，该员工可以胜任且最缺人的岗位
        Long skillIdToAssign = input.getRequirements().stream()
            .filter(req -> req.getShiftId().equals(bestShift.getId()) &&
                input.getUserSkills().getOrDefault(employee.getUserId(), Collections.emptyList()).contains(req.getSkillId()))
            .min(Comparator.comparingInt(req -> matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId())))
            .map(HrpScheduleRequirementsVo::getSkillId)
            .orElse(null);

        if (skillIdToAssign != null) {
            matrix.assignShift(employee.getUserId(), date, bestShift.getId(), skillIdToAssign);
        }
    }


    /**
     * 步骤 4: 填补所有剩余的人力缺口
     */
    private void fillRemainingShifts(ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        log.info("开始填补剩余的人力缺口...");
        long days = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;
        List<HrpScheduleRequirementsVo> sortedRequirements = sortRequirements(input);

        for (int i = 0; i < days; i++) {
            LocalDate currentDate = input.getStartDate().plusDays(i);
            String dayType = getDayType(currentDate, input.getStoreEvents());

            for (HrpScheduleRequirementsVo req : sortedRequirements) {
                // if (!req.getDayType().equals(dayType)) continue; // 动态需求统一按平日，所以注释掉

                int requiredCount = req.getRequiredCount();
                int assignedCount = matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                int deficit = requiredCount - assignedCount;

                if (deficit <= 0) continue;

                // 寻找所有符合基本条件的候选人
                List<HrpUserProfileVo> candidates = findCandidates(currentDate, req, matrix, input);
                // 根据排班模式从候选人中选择最优的几位
                List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, deficit, matrix, schedulingMode);

                for (HrpUserProfileVo employee : selectedEmployees) {
                    matrix.assignShift(employee.getUserId(), currentDate, req.getShiftId(), req.getSkillId());
                }
            }
        }
    }

    /**
     * 步骤 5: 为全职员工补足休息日并进行最终检查
     * @param matrix 排班矩阵
     * @param input 算法输入数据
     * @param feedbackItems 用于收集反馈信息的列表
     */
    private void finalizeFullTimeSchedules(ScheduleMatrix matrix, AlgorithmInput input, List<FeedbackItem> feedbackItems) {
        long totalDaysInPeriod = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;

        input.getEmployees().stream()
            .filter(e -> "全职".equals(e.getEmployeeType()))
            .forEach(emp -> {
                long workDays = matrix.getWorkDaysCount(emp.getUserId());
                long restDays = totalDaysInPeriod - workDays;

                // 【问题修复】: 此处修改为基于排班周期动态计算应休天数
                // 例如，按比例计算： (总天数 / 30) * 月度应休天数
                // 这里为了简化，我们假设每周应休2天
                long expectedRestDays = (long) Math.ceil((totalDaysInPeriod / 7.0) * 2);
                if(expectedRestDays == 0 && totalDaysInPeriod > 0) expectedRestDays = 1; // 至少休1天

                // 如果休息日不足，自动从后往前补充休息日
                if (restDays < expectedRestDays) {
                    long daysToBlock = expectedRestDays - restDays;
                    for (LocalDate date = input.getEndDate(); !date.isBefore(input.getStartDate()) && daysToBlock > 0; date = date.minusDays(1)) {
                        if (matrix.isAvailable(emp.getUserId(), date)) {
                            matrix.blockDay(emp.getUserId(), date, "补休");
                            daysToBlock--;
                        }
                    }
                }

                // 动态计算当前排班周期内的最低工时标准，而不是使用固定的月度值
                // 计算逻辑： (排班总天数 - 期望休息天数) * 每日平均工时 (假设为8小时)
                double minPeriodHours = (totalDaysInPeriod - expectedRestDays) * 8.0;
                double actualHours = matrix.getTotalHours(emp.getUserId());

                if (actualHours < minPeriodHours) {
                    String message = String.format("全职员工 [%s] 的最终排班总工时为 %.1f 小时，未达到当前周期最低标准(%.1f小时)。", emp.getUserName(), actualHours, minPeriodHours);
                    feedbackItems.add(FeedbackItem.builder()
                        .type(FeedbackItem.FeedbackType.CONSTRAINT_VIOLATION)
                        .severity(FeedbackItem.Severity.WARNING)
                        .message(message)
                        .relatedUserIds(Collections.singletonList(emp.getUserId()))
                        .build());
                    log.warn(message);
                }
            });
    }


    /**
     * 辅助方法: 寻找指定日期、班次、技能的所有候选人
     */
    private List<HrpUserProfileVo> findCandidates(LocalDate date, HrpScheduleRequirementsVo req, ScheduleMatrix matrix, AlgorithmInput input) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        HrpShiftsVo shift = input.getShiftsById().get(req.getShiftId());
        if (shift == null) return candidates;

        for (HrpUserProfileVo employee : input.getEmployees()) {
            Long userId = employee.getUserId();
            // 检查当天是否可用 (未休假或已排班)
            if (!matrix.isAvailable(userId, date)) continue;
            // 检查技能
            if (!input.getUserSkills().getOrDefault(userId, Collections.emptyList()).contains(req.getSkillId())) continue;
            // 检查可用时间段
            if (!isAvailableForShift(userId, date, shift, input)) continue;
            // 检查工作规则 (连续工作日、周工时上限等)
            if (matrix.getConsecutiveWorkDays(userId, date) >= MAX_CONSECUTIVE_WORK_DAYS) continue;
            // 对计时员工检查周工时上限
            if ("计时".equals(employee.getEmployeeType()) && matrix.getWeeklyHours(userId, date) + matrix.getShiftDuration(shift.getId()) > MAX_WEEKLY_HOURS) continue;
            // 检查班次连续性规则
            if (!matrix.isValidConsecutiveShift(userId, date, shift.getId())) continue;


            candidates.add(employee);
        }
        return candidates;
    }

    /**
     * 辅助方法: 检查员工在指定日期和班次是否可用
     */
    private boolean isAvailableForShift(Long userId, LocalDate date, HrpShiftsVo shift, AlgorithmInput input) {
        List<HrpUserAvailabilityVo> availabilities = input.getUserAvailabilities().get(userId);
        if (availabilities == null || availabilities.isEmpty()) return true; // 未设置则全天可用

        int dayOfWeek = date.getDayOfWeek().getValue();
        LocalTime shiftStart = shift.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime shiftEnd = shift.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

        for (HrpUserAvailabilityVo avail : availabilities) {
            if (avail.getDayOfWeek() == dayOfWeek) {
                LocalTime availStart = avail.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalTime availEnd = avail.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                if (!shiftStart.isBefore(availStart) && !shiftEnd.isAfter(availEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 辅助方法: 根据排班模式从候选人中选择最优的 N 位
     */
    private List<HrpUserProfileVo> selectBestCandidates(List<HrpUserProfileVo> candidates, int count, ScheduleMatrix matrix, String schedulingMode) {
        // 根据不同的排班模式，使用不同的排序比较器
        Comparator<HrpUserProfileVo> comparator;
        if ("AVERAGE".equals(schedulingMode)) {
            // 平均模式: 总工时少 -> 该岗位本期出勤少 -> 优先分数高 -> 随机
            comparator = Comparator.comparingDouble((HrpUserProfileVo e) -> matrix.getTotalHours(e.getUserId()))
                .thenComparingInt(e -> matrix.getSkillAssignedCount(e.getUserId(), -1L)) // 这里的-1L表示任意技能
                .thenComparing(HrpUserProfileVo::getPriorityScore, Comparator.reverseOrder());
        } else { // 默认为 PRIORITY 模式
            // 优先模式: 优先分数高 -> 总工时少 -> 随机
            comparator = Comparator.comparing(HrpUserProfileVo::getPriorityScore, Comparator.reverseOrder())
                .thenComparingDouble(e -> matrix.getTotalHours(e.getUserId()));
        }

        candidates.sort(comparator);
        return candidates.stream().limit(count).collect(Collectors.toList());
    }

    /**
     * 辅助方法: 对人力需求进行排序，优先处理稀缺技能的岗位
     */
    private List<HrpScheduleRequirementsVo> sortRequirements(AlgorithmInput input) {
        // 统计拥有每项技能的员工具体有几人
        Map<Long, Long> skillCounts = input.getUserSkills().values().stream()
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(skillId -> skillId, Collectors.counting()));

        // 排序逻辑：拥有该技能的人越少（技能越稀缺），优先级越高。如果稀缺度相同，则按班次开始时间排序。
        return input.getRequirements().stream()
            .sorted(Comparator.comparing((HrpScheduleRequirementsVo req) -> skillCounts.getOrDefault(req.getSkillId(), 0L))
                .thenComparing(req -> input.getShiftsById().get(req.getShiftId()).getStartTime()))
            .collect(Collectors.toList());
    }


    /**
     * 步骤 6: 生成结果并保存到数据库
     */
    private List<HrpSchedules> buildAndSaveResults(ScheduleMatrix matrix, ScheduleGenerateDto dto) {
        // 先删除该时间范围内的所有草稿排班，防止重复
        schedulesMapper.deleteDraftSchedules(dto.getStoreId(), dto.getStartDate(), dto.getEndDate());

        List<HrpSchedules> schedules = new ArrayList<>();
        for (Map.Entry<String, ScheduleAssignment> entry : matrix.getAssignments().entrySet()) {
            String[] keyParts = entry.getKey().split(":");
            HrpSchedules schedule = new HrpSchedules();
            schedule.setUserId(Long.parseLong(keyParts[0]));
            schedule.setScheduleDate(LocalDate.parse(keyParts[1]));
            schedule.setStoreId(dto.getStoreId());
            schedule.setShiftId(entry.getValue().getShiftId());
            schedule.setSkillId(entry.getValue().getSkillId());
            schedule.setStatus("DRAFT"); // 所有新生成的排班都是草稿状态
            schedules.add(schedule);
        }

        if (!schedules.isEmpty()) {
            schedulesMapper.insertBatch(schedules);
        }
        return schedules;
    }

    /**
     * 步骤 7: 生成人力缺口报告
     */
    private void generateUnsatisfiedRequirementsReport(ScheduleMatrix matrix, AlgorithmInput input, List<BreakCoverageRequirement> breakReqs, List<FeedbackItem> feedbackItems) {
        long days = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;

        // 检查常规班次缺口
        for (int i = 0; i < days; i++) {
            LocalDate currentDate = input.getStartDate().plusDays(i);
            for (HrpScheduleRequirementsVo req : input.getRequirements()) {
                int deficit = req.getRequiredCount() - matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                if (deficit > 0) {
                    String shiftName = input.getShiftsById().get(req.getShiftId()).getName();
                    String skillName = skillsMapper.selectById(req.getSkillId()).getName();
                    String message = String.format("日期 %s, 班次 [%s], 岗位 [%s], 缺少 %d 人", currentDate, shiftName, skillName, deficit);

                    feedbackItems.add(FeedbackItem.builder()
                        .type(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE)
                        .severity(FeedbackItem.Severity.ERROR)
                        .message(message)
                        .date(currentDate)
                        .shiftId(req.getShiftId())
                        .skillId(req.getSkillId())
                        .build());
                    log.warn(message);
                }
            }
        }

        // 检查休息时段替班缺口
        for (BreakCoverageRequirement req : breakReqs) {
            if (!req.isFulfilled()) {
                String skillName = skillsMapper.selectById(req.getSkillId()).getName();
                String message = String.format("日期 %s, 休息时段 [%s-%s], 岗位 [%s] 缺少替班人员", req.getDate(), req.getStartTime(), req.getEndTime(), skillName);

                feedbackItems.add(FeedbackItem.builder()
                    .type(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE)
                    .severity(FeedbackItem.Severity.WARNING) // 替班缺口通常为警告级别
                    .message(message)
                    .date(req.getDate())
                    .skillId(req.getSkillId())
                    .build());
                log.warn(message);
            }
        }
    }

    private List<HrpUserProfileVo> findCandidatesForBreak(BreakCoverageRequirement req, ScheduleMatrix matrix, AlgorithmInput input) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        for (HrpUserProfileVo employee : input.getEmployees()) {
            Long userId = employee.getUserId();
            // 替班者通常是计时工，且当天还没有其他安排
            if (!"计时".equals(employee.getEmployeeType()) || !matrix.isAvailable(userId, req.getDate())) {
                continue;
            }
            // 检查技能
            if (!input.getUserSkills().getOrDefault(userId, Collections.emptyList()).contains(req.getSkillId())) continue;
            // 检查特定时间段是否空闲
            if (!isAvailableForTimeRange(userId, req.getDate(), req.getStartTime(), req.getEndTime(), input)) continue;

            candidates.add(employee);
        }
        return candidates;
    }

    /**
     *  生成休息时段的替班需求
     * @param matrix 当前排班矩阵
     * @param input 算法输入数据
     * @return 需要被满足的休息时段替班需求列表
     */
    private List<BreakCoverageRequirement> generateBreakCoverageRequirements(ScheduleMatrix matrix, AlgorithmInput input) {
        log.info("开始生成休息时段的替班需求...");
        List<BreakCoverageRequirement> breakReqs = new ArrayList<>();

        // 遍历所有已安排的班次
        for (Map.Entry<String, ScheduleAssignment> entry : matrix.getAssignments().entrySet()) {
            String[] keyParts = entry.getKey().split(":");
            Long userId = Long.parseLong(keyParts[0]);
            LocalDate date = LocalDate.parse(keyParts[1]);
            ScheduleAssignment assignment = entry.getValue();

            // 只为全职员工的长时休息生成替班需求
            String employeeType = findEmployeeType(userId, input.getEmployees());
            if (!"全职".equals(employeeType)) {
                continue;
            }

            List<HrpShiftBreaksVo> breaks = input.getShiftBreaksByShiftId().get(assignment.getShiftId());
            if (breaks != null) {
                for (HrpShiftBreaksVo breakItem : breaks) {
                    long breakDuration = (breakItem.getBreakEndTime().getTime() - breakItem.getBreakStartTime().getTime()) / (60 * 1000);
                    if (breakDuration >= BREAK_COVERAGE_THRESHOLD_MINUTES) {
                        // 创建一个临时的替班需求
                        breakReqs.add(new BreakCoverageRequirement(
                            date,
                            breakItem.getBreakStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
                            breakItem.getBreakEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
                            assignment.getSkillId(), // 替班人员需要具备相同的技能
                            1, // 需求人数为1
                            false // 初始状态为未满足
                        ));
                        log.debug("为员工 {} 在 {} 的休息时段 ({}-{}) 生成了岗位 {} 的替班需求。", userId, date, breakItem.getBreakStartTime(), breakItem.getBreakEndTime(), assignment.getSkillId());
                    }
                }
            }
        }
        return breakReqs;
    }

    /**
     * 填充休息时段的替班人员
     */
    private void fillBreakCoverageShifts(List<BreakCoverageRequirement> breakReqs, ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        log.info("开始为 {} 个休息时段安排替班人员...", breakReqs.size());

        for (BreakCoverageRequirement req : breakReqs) {
            if (req.isFulfilled()) continue;

            // 寻找能满足这个特定时间段和技能的候选人（通常是计时员工）
            List<HrpUserProfileVo> candidates = findCandidatesForBreak(req, matrix, input);

            // 选出最优的替班者
            List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, 1, matrix, schedulingMode);

            if (!selectedEmployees.isEmpty()) {
                HrpUserProfileVo selected = selectedEmployees.get(0);
                // 注意：这里我们为替班人员创建一个特殊的排班记录
                // shiftId 和 skillId 都使用原始员工的信息，但 userId 是替班者的
                // 在前端展示时，可以根据这个“重叠”的排班记录渲染为“替班”状态
                matrix.assignShift(selected.getUserId(), req.getDate(), -req.getSkillId(), req.getSkillId()); // 使用负shiftId作为替班标记
                req.setFulfilled(true);
                log.info("员工 {} 被安排在 {} 替补岗位 {}", selected.getUserId(), req.getDate(), req.getSkillId());
            } else {
                log.warn("未能为日期 {} 时间 {}-{} 的岗位 {} 找到替班人员。", req.getDate(), req.getStartTime(), req.getEndTime(), req.getSkillId());
            }
        }
    }

    // --- 其他辅助方法 ---
    private String getDayType(LocalDate date, List<HrpStoreEventsVo> storeEvents) {
        for (HrpStoreEventsVo event : storeEvents) {
            if (event.getEventDate().isEqual(date)) {
                return "特殊节日";
            }
        }
        int dayOfWeek = date.getDayOfWeek().getValue();
        return (dayOfWeek >= 6) ? "假日" : "平日";
    }

    /**
     * 检查员工在特定日期和时间段内是否可用
     */
    private boolean isAvailableForTimeRange(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime, AlgorithmInput input) {
        List<HrpUserAvailabilityVo> availabilities = input.getUserAvailabilities().get(userId);
        if (availabilities == null || availabilities.isEmpty()) return true;

        int dayOfWeek = date.getDayOfWeek().getValue();
        for (HrpUserAvailabilityVo avail : availabilities) {
            if (avail.getDayOfWeek() == dayOfWeek) {
                LocalTime availStart = avail.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalTime availEnd = avail.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                if (!startTime.isBefore(availStart) && !endTime.isAfter(availEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String findEmployeeType(Long userId, List<HrpUserProfileVo> employees) {
        return employees.stream().filter(e -> e.getUserId().equals(userId)).findFirst()
            .map(HrpUserProfileVo::getEmployeeType).orElse("");
    }

    private String findEmployeeName(Long userId, List<HrpUserProfileVo> employees) {
        return employees.stream().filter(e -> e.getUserId().equals(userId)).findFirst()
            .map(HrpUserProfileVo::getUserName).orElse("未知员工");
    }

    // ================== 内部数据结构 ==================

    /**
     * 算法输入数据集合
     * 将所有从数据库查询出的数据统一封装，便于传递
     */
    @Data
    private static class AlgorithmInput {
        private final LocalDate startDate, endDate;
        private final Long storeId;
        private final String crossDayRule;
        private final List<HrpUserProfileVo> employees;
        private final Map<Long, List<Long>> userSkills;
        private final Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities;
        private final List<HrpLeaveRequestsVo> leaveRequests;
        private final List<HrpShiftsVo> shifts;
        private final Map<Long, HrpShiftsVo> shiftsById;
        private final Map<Long, List<HrpShiftBreaksVo>> shiftBreaksByShiftId;
        private final List<HrpScheduleRequirementsVo> requirements;
        private final List<HrpStoreEventsVo> storeEvents;

        public AlgorithmInput(LocalDate start, LocalDate end, Long storeId, String crossDayRule, List<HrpUserProfileVo> emp, Map<Long, List<Long>> skills, Map<Long, List<HrpUserAvailabilityVo>> avail, List<HrpLeaveRequestsVo> leaves, List<HrpShiftsVo> shifts, List<HrpShiftBreaksVo> breaks, List<HrpScheduleRequirementsVo> reqs, List<HrpStoreEventsVo> events) {
            this.startDate = start;
            this.endDate = end;
            this.storeId = storeId;
            this.crossDayRule = crossDayRule;
            this.employees = emp;
            this.userSkills = skills;
            this.userAvailabilities = avail;
            this.leaveRequests = leaves;
            this.shifts = shifts;
            this.requirements = reqs;
            this.storeEvents = events;
            this.shiftsById = shifts.stream().collect(Collectors.toMap(HrpShiftsVo::getId, s -> s));
            this.shiftBreaksByShiftId = breaks.stream().collect(Collectors.groupingBy(HrpShiftBreaksVo::getShiftId));
        }
    }

    /**
     * 单个排班分配记录
     */
    @Data
    @AllArgsConstructor
    private static class ScheduleAssignment {
        private Long shiftId;
        private Long skillId;
    }

    /**
     * 核心数据结构：排班矩阵
     * 用于在内存中进行排班计算和状态跟踪
     */
    private static class ScheduleMatrix {
        // Key: "userId:yyyy-MM-dd"
        private final Map<String, ScheduleAssignment> assignments = new HashMap<>();
        private final Map<String, String> blockedSlots = new HashMap<>();
        private final Map<Long, Double> totalHours;
        private final Map<Long, HrpShiftsVo> shiftsById;
        private final Map<Long, List<HrpShiftBreaksVo>> shiftBreaksByShiftId;

        public ScheduleMatrix(LocalDate start, LocalDate end, List<HrpUserProfileVo> employees, Map<Long, HrpShiftsVo> shiftsById, Map<Long, List<HrpShiftBreaksVo>> breaks) {
            this.totalHours = employees.stream().collect(Collectors.toMap(HrpUserProfileVo::getUserId, u -> 0.0));
            this.shiftsById = shiftsById;
            this.shiftBreaksByShiftId = breaks;
        }

        public void assignShift(Long userId, LocalDate date, Long shiftId, Long skillId) {
            String key = userId + ":" + date;
            assignments.put(key, new ScheduleAssignment(shiftId, skillId));
            blockedSlots.put(key, "已排班");
            totalHours.merge(userId, getShiftDuration(shiftId), Double::sum);
        }

        public void blockDay(Long userId, LocalDate date, String reason) {
            blockedSlots.put(userId + ":" + date, reason);
        }

        public boolean isAvailable(Long userId, LocalDate date) {
            return !blockedSlots.containsKey(userId + ":" + date);
        }

        public int getAssignedCount(LocalDate date, Long shiftId, Long skillId) {
            return (int) assignments.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith(":" + date))
                .map(Map.Entry::getValue)
                .filter(assignment -> assignment.getShiftId().equals(shiftId) && assignment.getSkillId().equals(skillId))
                .count();
        }

        public double getTotalHours(Long userId) {
            return totalHours.getOrDefault(userId, 0.0);
        }

        public double getWeeklyHours(Long userId, LocalDate currentDate) {
            LocalDate weekStart = currentDate.with(java.time.DayOfWeek.MONDAY);
            double weeklyHours = 0;
            for (int i = 0; i < 7; i++) {
                LocalDate date = weekStart.plusDays(i);
                ScheduleAssignment assignment = assignments.get(userId + ":" + date);
                if (assignment != null) {
                    weeklyHours += getShiftDuration(assignment.getShiftId());
                }
            }
            return weeklyHours;
        }

        public int getConsecutiveWorkDays(Long userId, LocalDate checkDate) {
            int consecutiveDays = 0;
            for (int i = 1; i <= MAX_CONSECUTIVE_WORK_DAYS + 1; i++) {
                LocalDate date = checkDate.minusDays(i);
                if (assignments.containsKey(userId + ":" + date)) {
                    consecutiveDays++;
                } else {
                    break;
                }
            }
            return consecutiveDays;
        }

        public long getBlockedDaysCount(Long userId) {
            return blockedSlots.keySet().stream().filter(k -> k.startsWith(userId + ":")).count();
        }

        public long getWorkDaysCount(Long userId) {
            return assignments.keySet().stream().filter(k -> k.startsWith(userId + ":")).count();
        }

        public int getSkillAssignedCount(Long userId, Long skillId) {
            return (int) assignments.entrySet().stream()
                .filter(e -> e.getKey().startsWith(userId + ":"))
                .map(Map.Entry::getValue)
                .filter(a -> skillId == -1L || a.getSkillId().equals(skillId))
                .count();
        }

        /**
         * 检查即将安排的班次是否与前一天的班次冲突
         * @return true 如果不冲突
         */
        public boolean isValidConsecutiveShift(Long userId, LocalDate date, Long newShiftId) {
            ScheduleAssignment yesterdayAssignment = assignments.get(userId + ":" + date.minusDays(1));
            if (yesterdayAssignment == null) {
                return true; // 前一天没上班，不冲突
            }

            // 示例规则：不允许 "打烊班" 之后紧接着上 "早班"
            HrpShiftsVo yesterdayShift = shiftsById.get(yesterdayAssignment.getShiftId());
            HrpShiftsVo todayShift = shiftsById.get(newShiftId);

            if (yesterdayShift != null && todayShift != null) {
                // 这里可以根据班别名称或代码定义冲突规则
                if (yesterdayShift.getName().contains("打烊") && todayShift.getName().contains("早")) {
                    return false; // 冲突
                }
            }
            return true;
        }

        /**
         * 计算班次的实际时长（扣除休息时间）
         */
        public double getShiftDuration(Long shiftId) {
            HrpShiftsVo shift = shiftsById.get(shiftId);
            if (shift == null) return 0.0;

            long durationMillis = shift.getEndTime().getTime() - shift.getStartTime().getTime();
            if (Boolean.TRUE.equals(shift.getIsCrossDay())) {
                durationMillis += 24 * 60 * 60 * 1000;
            }

            long breakMillis = 0;
            List<HrpShiftBreaksVo> breaks = shiftBreaksByShiftId.get(shiftId);
            if (breaks != null) {
                for (HrpShiftBreaksVo b : breaks) {
                    breakMillis += b.getBreakEndTime().getTime() - b.getBreakStartTime().getTime();
                }
            }

            return (durationMillis - breakMillis) / (1000.0 * 60 * 60);
        }

        public Map<String, ScheduleAssignment> getAssignments() {
            return this.assignments;
        }
    }

    /**
     * 内部数据结构: 用于表示一个临时的休息时段替班需求
     */
    @Data
    @AllArgsConstructor
    private static class BreakCoverageRequirement {
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long skillId;
        private int requiredCount;
        private boolean isFulfilled;
    }
}
