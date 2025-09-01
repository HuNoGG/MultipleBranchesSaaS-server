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
        if (input.getDailyRequirements().isEmpty()) {
            log.warn("所有日期的排班需求均为0, 排班任务中止。");
            feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.SYSTEM_WARNING).severity(FeedbackItem.Severity.WARNING).message("所有日期的排班需求均为0, 未生成任何排班。").build());
            return new ScheduleGenerationResult(new ArrayList<>(), feedbackItems);
        }

        // 2. 初始化排班矩阵并处理休假
        ScheduleMatrix matrix = initializeMatrix(input, dto, feedbackItems);

        // 3. 【核心步骤一】优先安排全职员工
        assignFullTimeEmployees(matrix, input, dto.getSchedulingMode());

        // TODO: #1 根据前端开关决定是否执行休息补替逻辑
        List<BreakCoverageRequirement> breakRequirements = new ArrayList<>();
        if (Boolean.TRUE.equals(dto.getEnableRestDaySubstitution())) {
            log.info("休息时段补替功能已启用。");
            breakRequirements = generateBreakCoverageRequirements(matrix, input);
            fillBreakCoverageShifts(breakRequirements, matrix, input, dto.getSchedulingMode());
        } else {
            log.info("休息时段补替功能已禁用。");
            feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.SYSTEM_WARNING).severity(FeedbackItem.Severity.WARNING).message("配置提示：本次排班未启用正职员工休息时段补替功能。").build());
        }

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

        List<Long> userIds = dto.getEmployees().stream()
            .map(ScheduleGenerateDto.EmployeeConfig::getId)
            .collect(Collectors.toList());
        if (userIds.isEmpty()) throw new ServiceException("请选择至少一名员工参与排班");

        // 查询员工、技能、可用时间、班次等基础信息
        List<HrpUserProfileVo> employees = userProfileMapper.selectVoByUserIds(userIds);
        Map<Long, List<Long>> userSkills = userSkillsMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserSkillsVo::getUserId, Collectors.mapping(HrpUserSkillsVo::getSkillId, Collectors.toList())));
        Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities = userAvailabilityMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserAvailabilityVo::getUserId));
        List<HrpLeaveRequestsVo> leaveRequests = leaveRequestsMapper.selectVoListByUsersAndDate(userIds, startDate, endDate);
        List<HrpShiftsVo> shifts = shiftsMapper.selectVoListByStoreId(storeId);
        List<HrpShiftBreaksVo> breaks = shiftBreaksMapper.selectVoList();

        // 将前端按天传递的需求转换为算法内部使用的 Map<LocalDate, List<...>> 结构
        Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyRequirements = convertDtoRequirements(dto.getRequirementsByDay(), shifts, feedbackItems);

        return new AlgorithmInput(startDate, endDate, storeId, employees, userSkills, userAvailabilities, leaveRequests, shifts, breaks, dailyRequirements);
    }

    /**
     * 辅助方法: 将DTO中的每日需求转换为算法所需的内部格式
     */
    private Map<LocalDate, List<HrpScheduleRequirementsVo>> convertDtoRequirements(Map<String, Map<String, Map<String, Integer>>> requirementsByDay, List<HrpShiftsVo> allShifts, List<FeedbackItem> feedbackItems) {
        if (requirementsByDay == null || requirementsByDay.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<LocalDate, List<HrpScheduleRequirementsVo>> resultMap = new HashMap<>();
        List<HrpSkills> allSkills = skillsMapper.selectList();
        Map<String, Long> skillNameToIdMap = allSkills.stream()
            .collect(Collectors.toMap(HrpSkills::getName, HrpSkills::getId, (e, r) -> e));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 遍历每一天的需求
        requirementsByDay.forEach((dateStr, timeSlotMap) -> {
            LocalDate date = LocalDate.parse(dateStr);
            List<HrpScheduleRequirementsVo> reqsForDate = new ArrayList<>();

            // 遍历当天的每个时间段 (班次)
            timeSlotMap.forEach((timeSlot, skillsMap) -> {
                Long shiftId = allShifts.stream()
                    .filter(s -> timeSlot.equals(s.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter) + "-" + s.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)))
                    .map(HrpShiftsVo::getId)
                    .findFirst()
                    .orElse(null);

                if (shiftId == null) return; // 跳过无法匹配的班次

                // 遍历每个岗位 (技能)
                skillsMap.forEach((skillName, count) -> {
                    Long skillId = skillNameToIdMap.get(skillName);
                    if (skillId != null && count > 0) {
                        HrpScheduleRequirementsVo req = new HrpScheduleRequirementsVo();
                        req.setDayType("动态"); // dayType不再重要, 因为是按天指定的
                        req.setShiftId(shiftId);
                        req.setSkillId(skillId);
                        req.setRequiredCount(count);
                        reqsForDate.add(req);
                    }
                });
            });
            if (!reqsForDate.isEmpty()) {
                resultMap.put(date, reqsForDate);
            }
        });
        return resultMap;
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
                    break;
                }
                if (!matrix.isAvailableForDay(employee.getUserId(), currentDate)) {
                    continue;
                }
                // TODO: 调整为可以为同一天安排多个班次
                findAndAssignBestShiftForEmployee(employee, currentDate, matrix, input, schedulingMode);
            }
        }
    }

    /**
     * 为单个员工在指定日期寻找并安排一个最合适的班次
     */
    private void findAndAssignBestShiftForEmployee(HrpUserProfileVo employee, LocalDate date, ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        final List<HrpScheduleRequirementsVo> requirementsForToday = input.getDailyRequirements().getOrDefault(date, Collections.emptyList());

        // 寻找所有潜在可上的班次
        List<HrpShiftsVo> possibleShifts = requirementsForToday.stream()
            .map(req -> input.getShiftsById().get(req.getShiftId()))
            .filter(Objects::nonNull)
            .distinct()
            .filter(shift -> matrix.isTimeRangeAvailable(employee.getUserId(), date, shift) && // 检查时间冲突
                isAvailableForShift(employee.getUserId(), date, shift, input) &&
                matrix.getConsecutiveWorkDays(employee.getUserId(), date) < MAX_CONSECUTIVE_WORK_DAYS)
            .collect(Collectors.toList());

        if (possibleShifts.isEmpty()) return;

        // 寻找最优班次 (这里简化为需求最紧迫的班次)
        possibleShifts.sort(Comparator.comparingInt((HrpShiftsVo shift) ->
            requirementsForToday.stream()
                .filter(req -> req.getShiftId().equals(shift.getId()))
                .mapToInt(req -> req.getRequiredCount() - matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId()))
                .sum()
        ).reversed());

        HrpShiftsVo bestShift = possibleShifts.get(0);

        // 寻找该班次下最缺人的、且该员工能胜任的岗位
        Long skillIdToAssign = requirementsForToday.stream()
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

        for (int i = 0; i < days; i++) {
            LocalDate currentDate = input.getStartDate().plusDays(i);

            //直接获取当天的需求, 不再通过 dayType 判断
            List<HrpScheduleRequirementsVo> requirementsForToday = input.getDailyRequirements().getOrDefault(currentDate, Collections.emptyList());
            List<HrpScheduleRequirementsVo> sortedRequirements = sortRequirements(requirementsForToday, input); // 对当天的需求排序

            for (HrpScheduleRequirementsVo req : sortedRequirements) {
                int requiredCount = req.getRequiredCount();
                int assignedCount = matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                int deficit = requiredCount - assignedCount;

                if (deficit <= 0) continue;

                List<HrpUserProfileVo> candidates = findCandidates(currentDate, req, matrix, input);
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
     * 辅助方法: 寻找指定日期、班次、技能的所有候选人 (已修改)
     */
    private List<HrpUserProfileVo> findCandidates(LocalDate date, HrpScheduleRequirementsVo req, ScheduleMatrix matrix, AlgorithmInput input) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        HrpShiftsVo shift = input.getShiftsById().get(req.getShiftId());
        if (shift == null) return candidates;

        for (HrpUserProfileVo employee : input.getEmployees()) {
            Long userId = employee.getUserId();
            // 检查当天是否被完全锁定 (例如休假)
            if (!matrix.isAvailableForDay(userId, date)) continue;

            // TODO:  检查该员工在当前班次的时间段内是否有空
            if (!matrix.isTimeRangeAvailable(userId, date, shift)) continue;

            // 检查技能
            if (!input.getUserSkills().getOrDefault(userId, Collections.emptyList()).contains(req.getSkillId())) continue;
            // 检查可用时间段
            if (!isAvailableForShift(userId, date, shift, input)) continue;
            // 检查工作规则 (连续工作日、周工时上限等)
            if (matrix.getConsecutiveWorkDays(userId, date) >= MAX_CONSECUTIVE_WORK_DAYS) continue;
            if ("计时".equals(employee.getEmployeeType()) && matrix.getWeeklyHours(userId, date) + matrix.getShiftDuration(shift.getId()) > MAX_WEEKLY_HOURS) continue;
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
    private List<HrpScheduleRequirementsVo> sortRequirements(List<HrpScheduleRequirementsVo> requirements, AlgorithmInput input) {
        Map<Long, Long> skillCounts = input.getUserSkills().values().stream()
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(skillId -> skillId, Collectors.counting()));

        return requirements.stream()
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
        for (Map.Entry<String, List<ScheduleAssignment>> entry : matrix.getAssignments().entrySet()) {
            for(ScheduleAssignment assignment : entry.getValue()){
                String[] keyParts = entry.getKey().split(":");
                HrpSchedules schedule = new HrpSchedules();
                schedule.setUserId(Long.parseLong(keyParts[0]));
                schedule.setScheduleDate(LocalDate.parse(keyParts[1]));
                schedule.setStoreId(dto.getStoreId());
                schedule.setShiftId(assignment.getShiftId());
                schedule.setSkillId(assignment.getSkillId());
                schedule.setStatus("DRAFT"); // 所有新生成的排班都是草稿状态
                schedules.add(schedule);
            }

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
        // TODO: // 修复点: 遍历每日需求来检查缺口, 而不是一个不存在的全局需求列表
        input.getDailyRequirements().forEach((currentDate, requirementsForToday) -> {
            for (HrpScheduleRequirementsVo req : requirementsForToday) {
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
        });

        // 检查休息时段替班缺口 (逻辑不变)
        for (BreakCoverageRequirement req : breakReqs) {
            if (!req.isFulfilled()) {
                String skillName = skillsMapper.selectById(req.getSkillId()).getName();
                String message = String.format("日期 %s, 休息时段 [%s-%s], 岗位 [%s] 缺少替班人员", req.getDate(), req.getStartTime(), req.getEndTime(), skillName);

                feedbackItems.add(FeedbackItem.builder()
                    .type(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE)
                    .severity(FeedbackItem.Severity.WARNING)
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
        for (Map.Entry<String, List<ScheduleAssignment>> entry : matrix.getAssignments().entrySet()) {
            String[] keyParts = entry.getKey().split(":");
            Long userId = Long.parseLong(keyParts[0]);
            LocalDate date = LocalDate.parse(keyParts[1]);
            for (ScheduleAssignment assignment : entry.getValue()) {

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

    @Data
    private static class AlgorithmInput {
        private final LocalDate startDate, endDate;
        private final Long storeId;
        private final List<HrpUserProfileVo> employees;
        private final Map<Long, List<Long>> userSkills;
        private final Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities;
        private final List<HrpLeaveRequestsVo> leaveRequests;
        private final List<HrpShiftsVo> shifts;
        private final Map<Long, HrpShiftsVo> shiftsById;
        private final Map<Long, List<HrpShiftBreaksVo>> shiftBreaksByShiftId;
        private final Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyRequirements;

        public AlgorithmInput(LocalDate start, LocalDate end, Long storeId, List<HrpUserProfileVo> emp, Map<Long, List<Long>> skills, Map<Long, List<HrpUserAvailabilityVo>> avail, List<HrpLeaveRequestsVo> leaves, List<HrpShiftsVo> shifts, List<HrpShiftBreaksVo> breaks, Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyReqs) {
            this.startDate = start;
            this.endDate = end;
            this.storeId = storeId;
            this.employees = emp;
            this.userSkills = skills;
            this.userAvailabilities = avail;
            this.leaveRequests = leaves;
            this.shifts = shifts;
            this.shiftsById = shifts.stream().collect(Collectors.toMap(HrpShiftsVo::getId, s -> s));
            this.shiftBreaksByShiftId = breaks.stream().collect(Collectors.groupingBy(HrpShiftBreaksVo::getShiftId));
            this.dailyRequirements = dailyReqs;
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
        // TODO: #1 Key: "userId:yyyy-MM-dd", Value: 当天所有班次的列表
        private final Map<String, List<ScheduleAssignment>> assignments = new HashMap<>();
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
            assignments.computeIfAbsent(key, k -> new ArrayList<>()).add(new ScheduleAssignment(shiftId, skillId));
            totalHours.merge(userId, getShiftDuration(shiftId), Double::sum);
        }

        public void blockDay(Long userId, LocalDate date, String reason) {
            blockedSlots.put(userId + ":" + date, reason);
        }

        public boolean isAvailable(Long userId, LocalDate date) {
            return !blockedSlots.containsKey(userId + ":" + date);
        }

        public boolean isAvailableForDay(Long userId, LocalDate date) {
            return !blockedSlots.containsKey(userId + ":" + date);
        }

        public boolean isTimeRangeAvailable(Long userId, LocalDate date, HrpShiftsVo newShift) {
            List<ScheduleAssignment> assigned = assignments.get(userId + ":" + date);
            if (CollectionUtils.isEmpty(assigned)) {
                return true; // 当天无任何安排, 肯定可用
            }

            LocalTime newStart = newShift.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime newEnd = newShift.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (newShift.getIsCrossDay()) newEnd = newEnd.plusHours(24);

            for (ScheduleAssignment assignment : assigned) {
                HrpShiftsVo existingShift = shiftsById.get(assignment.getShiftId());
                if (existingShift == null) continue;

                LocalTime existingStart = existingShift.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalTime existingEnd = existingShift.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                if (existingShift.getIsCrossDay()) existingEnd = existingEnd.plusHours(24);

                // 核心冲突检测: (StartA < EndB) and (EndA > StartB)
                if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                    return false; // 发现时间重叠, 不可用
                }
            }
            return true; // 没有发现任何时间重叠
        }

        public int getAssignedCount(LocalDate date, Long shiftId, Long skillId) {
            int count = 0;
            for (Map.Entry<String, List<ScheduleAssignment>> entry : assignments.entrySet()) {
                if (entry.getKey().endsWith(":" + date)) {
                    for(ScheduleAssignment assignment : entry.getValue()){
                        if (assignment.getShiftId().equals(shiftId) && assignment.getSkillId().equals(skillId)) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }

        public double getTotalHours(Long userId) {
            return totalHours.getOrDefault(userId, 0.0);
        }

        public double getWeeklyHours(Long userId, LocalDate currentDate) {
            LocalDate weekStart = currentDate.with(java.time.DayOfWeek.MONDAY);
            double weeklyHours = 0;
            for (int i = 0; i < 7; i++) {
                LocalDate date = weekStart.plusDays(i);
                List<ScheduleAssignment> assignmentList = assignments.get(userId + ":" + date);
                if (assignmentList != null) {
                    for(ScheduleAssignment assignment : assignmentList){
                        weeklyHours += getShiftDuration(assignment.getShiftId());
                    }
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
            int count = 0;
            for (Map.Entry<String, List<ScheduleAssignment>> entry : assignments.entrySet()) {
                if (entry.getKey().startsWith(userId + ":")) {
                    for(ScheduleAssignment assignment : entry.getValue()){
                        if (assignment.getSkillId().equals(skillId)) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }

        /**
         * 检查即将安排的班次是否与前一天的班次冲突
         * @return true 如果不冲突
         */
        public boolean isValidConsecutiveShift(Long userId, LocalDate date, Long newShiftId) {
            List<ScheduleAssignment> yesterdayAssignmentList = assignments.get(userId + ":" + date.minusDays(1));
            if (yesterdayAssignmentList == null) {
                return true; // 前一天没上班，不冲突
            }

            // 示例规则：不允许 "打烊班" 之后紧接着上 "早班"
            for(ScheduleAssignment yesterdayAssignment : yesterdayAssignmentList){
                HrpShiftsVo yesterdayShift = shiftsById.get(yesterdayAssignment.getShiftId());
                HrpShiftsVo todayShift = shiftsById.get(newShiftId);
                if (yesterdayShift != null && todayShift != null) {
                    // 这里可以根据班别名称或代码定义冲突规则
                    if (yesterdayShift.getName().contains("打烊") && todayShift.getName().contains("早")) {
                        return false; // 冲突
                    }
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

        public Map<String, List<ScheduleAssignment>> getAssignments() {
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
