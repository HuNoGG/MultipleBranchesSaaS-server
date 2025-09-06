package org.dromara.hrp.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.hrp.domain.HrpSkills;
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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能排班算法核心服务 (已重构和修复)
 * @author Lion Li & Jules
 */
@Service
public class SchedulingAlgorithmService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingAlgorithmService.class);

    // --- 算法可配置参数 (未来应移至数据库或配置中心) ---
    private static final int MAX_CONSECUTIVE_WORK_DAYS = 6;     // 最大连续工作天数
    private static final double MAX_WEEKLY_HOURS = 40.0;        // 计时员工每周最大工时
    private static final int MAX_FULLTIME_LEAVE_PER_DAY = 2;    // 每日最多允许的正职休假人数
    private static final int FULL_TIME_MONTHLY_REST_DAYS = 8;   // 正职员工每月最少休息天数
    private static final long BREAK_COVERAGE_THRESHOLD_MINUTES = 90; // 超过此分钟数的休息需要安排替班

    // --- 依赖注入 ---
    @Autowired private HrpUserProfileMapper userProfileMapper;
    @Autowired private HrpUserSkillsMapper userSkillsMapper;
    @Autowired private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Autowired private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Autowired private HrpScheduleRequirementsMapper scheduleRequirementsMapper;
    @Autowired private HrpShiftsMapper shiftsMapper;
    @Autowired private HrpSchedulesMapper schedulesMapper;
    @Autowired private HrpSkillsMapper skillsMapper;
    @Autowired private HrpShiftBreaksMapper shiftBreaksMapper;

    /**
     * 智能排班算法主入口
     */
    @Transactional(rollbackFor = Exception.class)
    public ScheduleGenerationResult generateSchedule(ScheduleGenerateDto dto) {
        List<FeedbackItem> feedbackItems = new ArrayList<>();
        String schedulingMode = StringUtils.hasText(dto.getSchedulingMode()) ? dto.getSchedulingMode() : "PRIORITY";
        log.info("智能排班任务启动 V4.0，分店ID: {}, 日期范围: {} to {}, 排班模式: {}", dto.getStoreId(), dto.getStartDate(), dto.getEndDate(), schedulingMode);
        int maxConsecutiveWorkDays = dto.getMaxConsecutiveWorkDays() != null ? dto.getMaxConsecutiveWorkDays() : MAX_CONSECUTIVE_WORK_DAYS;


        AlgorithmInput input = prepareInputData(dto, feedbackItems);
        if (input.getDailyRequirements().isEmpty()) {
            feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.SYSTEM_WARNING).severity(FeedbackItem.Severity.WARNING).message("所有日期的排班需求均为0, 未生成任何排班。").build());
            return new ScheduleGenerationResult(new ArrayList<>(), feedbackItems);
        }

        ScheduleMatrix matrix = initializeMatrix(input, dto, feedbackItems, maxConsecutiveWorkDays);
        assignFullTimeEmployees(matrix, input, schedulingMode, maxConsecutiveWorkDays);
        List<BreakCoverageRequirement> breakReqs = assignBreakCoverage(matrix, input, schedulingMode);
        assignPartTimeAndOtherEmployees(matrix, input, schedulingMode, maxConsecutiveWorkDays);
        finalizeFullTimeSchedules(matrix, input, feedbackItems);
        List<HrpSchedules> generatedSchedules = buildAndSaveResults(matrix, dto);
        generateUnsatisfiedRequirementsReport(matrix, input, breakReqs, feedbackItems);

        log.info("智能排班任务完成，共生成 {} 条排班记录。", generatedSchedules.size());
        return new ScheduleGenerationResult(generatedSchedules, feedbackItems);
    }

    private AlgorithmInput prepareInputData(ScheduleGenerateDto dto, List<FeedbackItem> feedbackItems) {
        Long storeId = dto.getStoreId();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Long> userIds = dto.getEmployees().stream()
            .map(ScheduleGenerateDto.EmployeeConfig::getId)
            .collect(Collectors.toList());
        if (userIds.isEmpty()) throw new ServiceException("请选择至少一名员工参与排班");

        List<HrpUserProfileVo> employees = userProfileMapper.selectVoByUserIds(userIds);
        Map<Long, List<HrpUserSkillsVo>> userSkills = userSkillsMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserSkillsVo::getUserId));
        Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities = userAvailabilityMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserAvailabilityVo::getUserId));
        List<HrpLeaveRequestsVo> leaveRequests = leaveRequestsMapper.selectVoListByUsersAndDate(userIds, startDate, endDate);
        List<HrpShiftsVo> shifts = shiftsMapper.selectVoListByStoreId(storeId);
        List<HrpShiftBreaksVo> breaks = shiftBreaksMapper.selectVoList();

        List<HrpSchedulesVo> pastSchedules = schedulesMapper.selectWorkDatesForUsersBefore(userIds, startDate, MAX_CONSECUTIVE_WORK_DAYS);
        Map<Long, Set<LocalDate>> pastWorkDays = pastSchedules.stream()
            .collect(Collectors.groupingBy(
                HrpSchedulesVo::getUserId,
                Collectors.mapping(HrpSchedulesVo::getScheduleDate, Collectors.toSet())
            ));

        Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyRequirements = convertDtoRequirements(dto.getRequirementsByDay(), shifts, feedbackItems);
        return new AlgorithmInput(startDate, endDate, storeId, employees, userSkills, userAvailabilities, leaveRequests, shifts, breaks, dailyRequirements, pastWorkDays);
    }

    private List<BreakCoverageRequirement> assignBreakCoverage(ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        log.info("【阶段二】开始为正职员工的长时休息安排替班...");
        List<BreakCoverageRequirement> breakReqs = generateBreakCoverageRequirements(matrix, input);
        for (BreakCoverageRequirement req : breakReqs) {
            List<HrpUserProfileVo> candidates = findCandidatesForBreak(req, matrix, input);
            if (!candidates.isEmpty()) {
                List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, 1, matrix, req.getSkillId(), input, schedulingMode);
                if (CollectionUtils.isEmpty(selectedEmployees)) continue;
                HrpUserProfileVo selectedEmployee = selectedEmployees.get(0);
                Map<String, Object> remarkMap = new HashMap<>();
                remarkMap.put("type", "BREAK_COVERAGE");
                remarkMap.put("substitutedUserId", req.getOriginalUserId());
                remarkMap.put("substitutedUserName", input.getEmployees().stream()
                    .filter(e -> e.getUserId().equals(req.getOriginalUserId()))
                    .findFirst().map(HrpUserProfileVo::getUserName).orElse("未知员工"));
                String remarkJson = JSONUtil.toJsonStr(remarkMap);
                matrix.assignBreakCoverage(selectedEmployee.getUserId(), req.getDate(), req.getStartTime(), req.getEndTime(), req.getSkillId(), remarkJson);
                req.setFulfilled(true);
            }
        }
        return breakReqs;
    }

    private Map<LocalDate, List<HrpScheduleRequirementsVo>> convertDtoRequirements(Map<String, Map<String, Map<String, Integer>>> requirementsByDay, List<HrpShiftsVo> allShifts, List<FeedbackItem> feedbackItems) {
        if (requirementsByDay == null || requirementsByDay.isEmpty()) return Collections.emptyMap();
        Map<LocalDate, List<HrpScheduleRequirementsVo>> resultMap = new HashMap<>();
        List<HrpSkills> allSkills = skillsMapper.selectList();
        Map<String, Long> skillNameToIdMap = allSkills.stream().collect(Collectors.toMap(HrpSkills::getName, HrpSkills::getId, (e, r) -> e));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        requirementsByDay.forEach((dateStr, timeSlotMap) -> {
            LocalDate date = LocalDate.parse(dateStr);
            List<HrpScheduleRequirementsVo> reqsForDate = new ArrayList<>();
            timeSlotMap.forEach((timeSlot, skillsMap) -> {
                Long shiftId = allShifts.stream()
                    .filter(s -> timeSlot.equals(s.getStartTime().format(timeFormatter) + "-" + s.getEndTime().format(timeFormatter)))
                    .map(HrpShiftsVo::getId).findFirst().orElse(null);
                if (shiftId == null) return;
                skillsMap.forEach((skillName, count) -> {
                    Long skillId = skillNameToIdMap.get(skillName);
                    if (skillId != null && count > 0) {
                        HrpScheduleRequirementsVo req = new HrpScheduleRequirementsVo();
                        req.setDayType("动态");
                        req.setShiftId(shiftId);
                        req.setSkillId(skillId);
                        req.setRequiredCount(count);
                        reqsForDate.add(req);
                    }
                });
            });
            if (!reqsForDate.isEmpty()) resultMap.put(date, reqsForDate);
        });
        return resultMap;
    }

    private ScheduleMatrix initializeMatrix(AlgorithmInput input, ScheduleGenerateDto dto, List<FeedbackItem> feedbackItems, int maxConsecutiveWorkDays) {
        ScheduleMatrix matrix = new ScheduleMatrix(input.getStartDate(), input.getEndDate(), input.getEmployees(), input.getShiftsById(), input.getShiftBreaksByShiftId(), input.getPastWorkDays(), maxConsecutiveWorkDays);
        handleFullTimeLeaveConflicts(input, feedbackItems);
        for (HrpLeaveRequestsVo leave : input.getLeaveRequests()) {
            if ("已提交".equals(leave.getApprovalStatus()) || "已锁定".equals(leave.getApprovalStatus())) {
                matrix.blockDay(leave.getUserId(), leave.getLeaveDate(), "休假");
            }
        }
        for (ScheduleGenerateDto.EmployeeConfig empConfig : dto.getEmployees()) {
            if (empConfig.getDaysOff() != null) {
                for (String dayOffLabel : empConfig.getDaysOff()) {
                    LocalDate date = findDateByLabel(dayOffLabel, input.getStartDate(), input.getEndDate());
                    if (date != null) matrix.blockDay(empConfig.getId(), date, "休息");
                }
            }
        }
        long totalDaysInPeriod = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;
        input.getEmployees().stream().filter(e -> "正职".equals(e.getEmployeeType())).forEach(emp -> {
            long blockedDays = matrix.getBlockedDaysCount(emp.getUserId());
            long availableWorkDays = totalDaysInPeriod - blockedDays;
            if (availableWorkDays < (totalDaysInPeriod - FULL_TIME_MONTHLY_REST_DAYS)) {
                String message = String.format("正职员工 [%s] 的预设休假过多(%d天)，可能无法满足月度最低工时要求。", emp.getUserName(), blockedDays);
                feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.CONSTRAINT_VIOLATION).severity(FeedbackItem.Severity.WARNING).message(message).relatedUserIds(Collections.singletonList(emp.getUserId())).build());
                log.warn(message);
            }
        });
        return matrix;
    }

    private LocalDate findDateByLabel(String label, LocalDate startDate, LocalDate endDate) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String currentLabel = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.CHINESE) + " (" + date.getMonthValue() + "." + date.getDayOfMonth() + ")";
            if (label.equals(currentLabel)) return date;
        }
        return null;
    }

    private void handleFullTimeLeaveConflicts(AlgorithmInput input, List<FeedbackItem> feedbackItems) {
        Map<LocalDate, List<HrpLeaveRequestsVo>> fullTimeLeaveMap = input.getLeaveRequests().stream()
            .filter(lr -> "正职".equals(findEmployeeType(lr.getUserId(), input.getEmployees())) && "已提交".equals(lr.getApprovalStatus()))
            .collect(Collectors.groupingBy(HrpLeaveRequestsVo::getLeaveDate));

        fullTimeLeaveMap.forEach((date, requests) -> {
            if (requests.size() > MAX_FULLTIME_LEAVE_PER_DAY) {
                log.warn("日期 [{}] 的正职员工休假申请人数 ({}) 超过上限 ({}), 进行抽签处理...", date, requests.size(), MAX_FULLTIME_LEAVE_PER_DAY);
                Collections.shuffle(requests);
                for (int i = MAX_FULLTIME_LEAVE_PER_DAY; i < requests.size(); i++) {
                    HrpLeaveRequestsVo deniedRequest = requests.get(i);
                    deniedRequest.setApprovalStatus("已取消");
                    String employeeName = findEmployeeName(deniedRequest.getUserId(), input.getEmployees());
                    String message = String.format("因超出每日正职休假上限，员工 [%s] 于 %s 的休假申请已被系统自动取消。", employeeName, date);
                    feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.SYSTEM_WARNING).severity(FeedbackItem.Severity.WARNING).message(message).date(date).relatedUserIds(Collections.singletonList(deniedRequest.getUserId())).build());
                    log.info(message);
                }
            }
        });
    }

    private void assignFullTimeEmployees(ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode, int maxConsecutiveWorkDays) {
        log.info("【阶段一】开始为正职员工排班 (模式: {})", schedulingMode);
        for (LocalDate currentDate = input.getStartDate(); !currentDate.isAfter(input.getEndDate()); currentDate = currentDate.plusDays(1)) {
            List<HrpScheduleRequirementsVo> requirementsForToday = input.getDailyRequirements().getOrDefault(currentDate, Collections.emptyList());
            List<HrpScheduleRequirementsVo> sortedRequirements = sortRequirementsByUrgency(requirementsForToday, matrix, currentDate, input);
            for (HrpScheduleRequirementsVo req : sortedRequirements) {
                int deficit = req.getRequiredCount() - matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                if (deficit <= 0) continue;
                List<HrpUserProfileVo> candidates = findCandidatesForRequirement(currentDate, req, matrix, input, "正职", maxConsecutiveWorkDays);
                List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, deficit, matrix, req.getSkillId(), input, schedulingMode);
                for (HrpUserProfileVo employee : selectedEmployees) {
                    matrix.assignShift(employee.getUserId(), currentDate, req.getShiftId(), req.getSkillId());
                }
            }
        }
    }

    private void findAndAssignBestShiftForEmployee(HrpUserProfileVo employee, LocalDate date, ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode) {
        final List<HrpScheduleRequirementsVo> requirementsForToday = input.getDailyRequirements().getOrDefault(date, Collections.emptyList());
        List<HrpShiftsVo> possibleShifts = requirementsForToday.stream()
            .map(req -> input.getShiftsById().get(req.getShiftId())).filter(Objects::nonNull).distinct()
            .filter(shift -> matrix.isTimeRangeAvailable(employee.getUserId(), date, shift) &&
                isAvailableForShift(employee, date, shift, input) &&
                matrix.getConsecutiveWorkDays(employee.getUserId(), date) < MAX_CONSECUTIVE_WORK_DAYS)
            .collect(Collectors.toList());
        if (possibleShifts.isEmpty()) return;
        possibleShifts.sort(Comparator.comparingInt((HrpShiftsVo shift) ->
            requirementsForToday.stream()
                .filter(req -> req.getShiftId().equals(shift.getId()))
                .mapToInt(req -> req.getRequiredCount() - matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId())).sum()
        ).reversed());
        HrpShiftsVo bestShift = possibleShifts.get(0);
        Long skillIdToAssign = requirementsForToday.stream()
            .filter(req -> req.getShiftId().equals(bestShift.getId()) &&
                input.getUserSkills().getOrDefault(employee.getUserId(), Collections.emptyList()).stream().anyMatch(s -> s.getSkillId().equals(req.getSkillId())))
            .min(Comparator.comparingInt(req -> matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId())))
            .map(HrpScheduleRequirementsVo::getSkillId).orElse(null);
        if (skillIdToAssign != null) {
            matrix.assignShift(employee.getUserId(), date, bestShift.getId(), skillIdToAssign);
        }
    }

    private void assignPartTimeAndOtherEmployees(ScheduleMatrix matrix, AlgorithmInput input, String schedulingMode, int maxConsecutiveWorkDays) {
        log.info("【阶段三】开始使用兼职及其他员工补充剩余的完整班次缺口 (模式: {})", schedulingMode);
        for (LocalDate currentDate = input.getStartDate(); !currentDate.isAfter(input.getEndDate()); currentDate = currentDate.plusDays(1)) {
            List<HrpScheduleRequirementsVo> requirementsForToday = input.getDailyRequirements().getOrDefault(currentDate, Collections.emptyList());
            List<HrpScheduleRequirementsVo> sortedRequirements = sortRequirementsByUrgency(requirementsForToday, matrix, currentDate, input);
            for (HrpScheduleRequirementsVo req : sortedRequirements) {
                int deficit = req.getRequiredCount() - matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                if (deficit <= 0) continue;
                List<HrpUserProfileVo> candidates = findCandidatesForRequirement(currentDate, req, matrix, input, "兼职", maxConsecutiveWorkDays);
                List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, deficit, matrix, req.getSkillId(), input, schedulingMode);
                for (HrpUserProfileVo employee : selectedEmployees) {
                    matrix.assignShift(employee.getUserId(), currentDate, req.getShiftId(), req.getSkillId());
                }
            }
        }
    }

    private List<HrpUserProfileVo> findCandidatesForRequirement(LocalDate date, HrpScheduleRequirementsVo req, ScheduleMatrix matrix, AlgorithmInput input, String employeeTypeFilter, int maxConsecutiveWorkDays) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        HrpShiftsVo shift = input.getShiftsById().get(req.getShiftId());
        if (shift == null) return candidates;
        for (HrpUserProfileVo employee : input.getEmployees()) {
            if (employeeTypeFilter != null) {
                if ("正职".equals(employeeTypeFilter) && !"正职".equals(employee.getEmployeeType())) continue;
                if ("兼职".equals(employeeTypeFilter) && "正职".equals(employee.getEmployeeType())) continue;
            }
            Long userId = employee.getUserId();
            if (!matrix.isAvailableForDay(userId, date)) continue;
            if (!matrix.isTimeRangeAvailable(userId, date, shift)) continue;
            if (input.getUserSkills().getOrDefault(userId, Collections.emptyList()).stream().noneMatch(s -> s.getSkillId().equals(req.getSkillId()))) continue;
            if (!isAvailableForShift(employee, date, shift, input)) continue;
            if (matrix.getConsecutiveWorkDays(userId, date) >= maxConsecutiveWorkDays) continue;
            candidates.add(employee);
        }
        return candidates;
    }

    private void finalizeFullTimeSchedules(ScheduleMatrix matrix, AlgorithmInput input, List<FeedbackItem> feedbackItems) {
        long totalDaysInPeriod = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;
        input.getEmployees().stream().filter(e -> "正职".equals(e.getEmployeeType())).forEach(emp -> {
            long workDays = matrix.getWorkDaysCount(emp.getUserId());
            long restDays = totalDaysInPeriod - workDays;
            long expectedRestDays = (long) Math.ceil((totalDaysInPeriod / 7.0) * 2);
            if(expectedRestDays == 0 && totalDaysInPeriod > 0) expectedRestDays = 1;
            if (restDays < expectedRestDays) {
                long daysToBlock = expectedRestDays - restDays;
                for (LocalDate date = input.getEndDate(); !date.isBefore(input.getStartDate()) && daysToBlock > 0; date = date.minusDays(1)) {
                    if (matrix.isAvailable(emp.getUserId(), date)) {
                        matrix.blockDay(emp.getUserId(), date, "补休");
                        daysToBlock--;
                    }
                }
            }
            double minPeriodHours = (totalDaysInPeriod - expectedRestDays) * 8.0;
            double actualHours = matrix.getTotalHours(emp.getUserId());
            if (actualHours < minPeriodHours) {
                String message = String.format("正职员工 [%s] 的最终排班总工时为 %.1f 小时，未达到当前周期最低标准(%.1f小时)。", emp.getUserName(), actualHours, minPeriodHours);
                feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.CONSTRAINT_VIOLATION).severity(FeedbackItem.Severity.WARNING).message(message).relatedUserIds(Collections.singletonList(emp.getUserId())).build());
                log.warn(message);
            }
        });
    }

    private List<HrpUserProfileVo> findCandidates(LocalDate date, HrpScheduleRequirementsVo req, ScheduleMatrix matrix, AlgorithmInput input) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        HrpShiftsVo shift = input.getShiftsById().get(req.getShiftId());
        if (shift == null) return candidates;
        for (HrpUserProfileVo employee : input.getEmployees()) {
            Long userId = employee.getUserId();
            if (!matrix.isAvailableForDay(userId, date)) continue;
            if (!matrix.isTimeRangeAvailable(userId, date, shift)) continue;
            if (input.getUserSkills().getOrDefault(userId, Collections.emptyList()).stream().noneMatch(s -> s.getSkillId().equals(req.getSkillId()))) continue;
            if (!isAvailableForShift(employee, date, shift, input)) continue;
            if (matrix.getConsecutiveWorkDays(userId, date) >= MAX_CONSECUTIVE_WORK_DAYS) continue;
            if ("计时".equals(employee.getEmployeeType()) && matrix.getWeeklyHours(userId, date) + matrix.getShiftDuration(shift.getId()) > MAX_WEEKLY_HOURS) continue;
            if (!matrix.isValidConsecutiveShift(userId, date, shift.getId())) continue;
            candidates.add(employee);
        }
        return candidates;
    }

    private boolean isAvailableForShift(HrpUserProfileVo employee, LocalDate date, HrpShiftsVo shift, AlgorithmInput input) {
        Long userId = employee.getUserId();
        List<HrpUserAvailabilityVo> availabilities = input.getUserAvailabilities().get(userId);
        if (CollectionUtils.isEmpty(availabilities)) return true;
        int dayOfWeek = date.getDayOfWeek().getValue();
        LocalTime shiftStart = shift.getStartTime();
        LocalTime shiftEnd = shift.getEndTime();
        for (HrpUserAvailabilityVo avail : availabilities) {
            if (avail.getDayOfWeek() == dayOfWeek) {
                LocalTime availStart = avail.getStartTime();
                LocalTime availEnd = avail.getEndTime();
                if (!shiftStart.isBefore(availStart) && !shiftEnd.isAfter(availEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPartTimerAllDayAvailable(HrpUserProfileVo employee, AlgorithmInput input) {
        if (!"兼职".equals(employee.getEmployeeType())) return false;
        return CollectionUtils.isEmpty(input.getUserAvailabilities().get(employee.getUserId()));
    }

    private Long getSkillPriority(Long userId, Long skillId, AlgorithmInput input) {
        return input.getUserSkills().getOrDefault(userId, Collections.emptyList()).stream()
            .filter(s -> s.getSkillId().equals(skillId))
            .map(HrpUserSkillsVo::getPriority).filter(Objects::nonNull).findFirst().orElse(0L);
    }

    private List<HrpUserProfileVo> selectBestCandidates(List<HrpUserProfileVo> candidates, int count, ScheduleMatrix matrix, Long skillId, AlgorithmInput input, String schedulingMode) {
        Comparator<HrpUserProfileVo> primaryComparator = Comparator
            .comparing((HrpUserProfileVo e) -> "正职".equals(e.getEmployeeType()) ? 0 : 1)
            .thenComparing(e -> isPartTimerAllDayAvailable(e, input) ? 0 : 1)
            .thenComparing(e -> getSkillPriority(e.getUserId(), skillId, input), Comparator.reverseOrder());
        Comparator<HrpUserProfileVo> tieBreaker;
        switch (schedulingMode) {
            case "AVERAGE":
                tieBreaker = Comparator.comparingDouble((HrpUserProfileVo e) -> matrix.getTotalHours(e.getUserId()))
                    .thenComparingInt(e -> matrix.getSkillAssignedCount(e.getUserId(), skillId))
                    .thenComparing(HrpUserProfileVo::getPriorityScore, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "PRIORITY":
            default:
                tieBreaker = Comparator.comparing(HrpUserProfileVo::getPriorityScore, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingDouble(e -> matrix.getTotalHours(e.getUserId()));
                break;
        }
        Comparator<HrpUserProfileVo> finalComparator = primaryComparator.thenComparing(tieBreaker);
        // TODO: 可在此处为后续更复杂的次排序规则（如员工评价、成本等）预留扩展点
        candidates.sort(finalComparator);
        return candidates.stream().limit(count).collect(Collectors.toList());
    }

    private List<HrpScheduleRequirementsVo> sortRequirementsByUrgency(List<HrpScheduleRequirementsVo> requirements, ScheduleMatrix matrix, LocalDate date, AlgorithmInput input) {
        return requirements.stream()
            .sorted(Comparator.comparing((HrpScheduleRequirementsVo req) -> input.getShiftsById().get(req.getShiftId()).getStartTime())
                .thenComparing(
                    Comparator.comparingInt((HrpScheduleRequirementsVo req) ->
                        req.getRequiredCount() - matrix.getAssignedCount(date, req.getShiftId(), req.getSkillId())
                    ).reversed()
                ))
            .collect(Collectors.toList());
    }

    private List<HrpScheduleRequirementsVo> sortRequirements(List<HrpScheduleRequirementsVo> requirements, AlgorithmInput input) {
        Map<Long, Long> skillCounts = input.getUserSkills().values().stream()
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(HrpUserSkillsVo::getSkillId, Collectors.counting()));
        return requirements.stream()
            .sorted(Comparator.comparing((HrpScheduleRequirementsVo req) -> skillCounts.getOrDefault(req.getSkillId(), 0L))
                .thenComparing(req -> input.getShiftsById().get(req.getShiftId()).getStartTime()))
            .collect(Collectors.toList());
    }

    private List<HrpSchedules> buildAndSaveResults(ScheduleMatrix matrix, ScheduleGenerateDto dto) {
        schedulesMapper.deleteDraftSchedules(dto.getStoreId(), dto.getStartDate(), dto.getEndDate());
        List<HrpSchedules> schedules = new ArrayList<>();
        matrix.getAssignments().forEach((key, assignments) -> {
            for (ScheduleAssignment assignment : assignments) {
                String[] keyParts = key.split(":");
                HrpSchedules schedule = new HrpSchedules();
                schedule.setUserId(Long.parseLong(keyParts[0]));
                schedule.setScheduleDate(LocalDate.parse(keyParts[1]));
                schedule.setStoreId(dto.getStoreId());
                schedule.setShiftId(assignment.getShiftId());
                schedule.setSkillId(assignment.getSkillId());
                HrpShiftsVo shift = matrix.getShiftsById().get(assignment.getShiftId());
                if (shift != null) {
                    schedule.setStartTime(shift.getStartTime());
                    schedule.setEndTime(shift.getEndTime());
                }
                schedule.setStatus("DRAFT");
                schedules.add(schedule);
            }
        });
        matrix.getBreakCoverageAssignments().forEach((key, assignments) -> {
            for (BreakAssignment assignment : assignments) {
                String[] keyParts = key.split(":");
                HrpSchedules schedule = new HrpSchedules();
                schedule.setUserId(Long.parseLong(keyParts[0]));
                schedule.setScheduleDate(LocalDate.parse(keyParts[1]));
                schedule.setStoreId(dto.getStoreId());
                schedule.setSkillId(assignment.getSkillId());
                schedule.setShiftId(null);
                schedule.setStartTime(assignment.getStartTime());
                schedule.setEndTime(assignment.getEndTime());
                schedule.setRemark(assignment.getRemark());
                schedule.setStatus("DRAFT");
                schedules.add(schedule);
            }
        });
        if (!schedules.isEmpty()) schedulesMapper.insertBatch(schedules);
        return schedules;
    }

    private void generateUnsatisfiedRequirementsReport(ScheduleMatrix matrix, AlgorithmInput input, List<BreakCoverageRequirement> breakReqs, List<FeedbackItem> feedbackItems) {
        input.getDailyRequirements().forEach((currentDate, requirementsForToday) -> {
            for (HrpScheduleRequirementsVo req : requirementsForToday) {
                int deficit = req.getRequiredCount() - matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                if (deficit > 0) {
                    HrpShiftsVo shift = input.getShiftsById().get(req.getShiftId());
                    if (shift != null) {
                        String shiftName = shift.getName();
                        String skillName = skillsMapper.selectById(req.getSkillId()).getName();
                        String message = String.format("日期 %s, 班次 [%s], 岗位 [%s], 缺少 %d 人", currentDate, shiftName, skillName, deficit);
                        feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE).severity(FeedbackItem.Severity.ERROR).message(message).date(currentDate).shiftId(req.getShiftId()).skillId(req.getSkillId()).build());
                        log.warn(message);
                    }
                }
            }
        });
        for (BreakCoverageRequirement req : breakReqs) {
            if (!req.isFulfilled()) {
                String skillName = skillsMapper.selectById(req.getSkillId()).getName();
                String message = String.format("日期 %s, 休息时段 [%s-%s], 岗位 [%s] 缺少替班人员", req.getDate(), req.getStartTime(), req.getEndTime(), skillName);
                feedbackItems.add(FeedbackItem.builder().type(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE).severity(FeedbackItem.Severity.WARNING).message(message).date(req.getDate()).skillId(req.getSkillId()).build());
                log.warn(message);
            }
        }
    }

    private List<HrpUserProfileVo> findCandidatesForBreak(BreakCoverageRequirement req, ScheduleMatrix matrix, AlgorithmInput input) {
        List<HrpUserProfileVo> candidates = new ArrayList<>();
        for (HrpUserProfileVo employee : input.getEmployees()) {
            if (!"兼职".equals(employee.getEmployeeType())) continue;
            Long userId = employee.getUserId();
            if (!matrix.isAvailableForDay(userId, req.getDate())) continue;
            if (!matrix.isTimeRangeAvailableForBreak(userId, req.getDate(), req.getStartTime(), req.getEndTime())) continue;
            if (input.getUserSkills().getOrDefault(userId, Collections.emptyList()).stream().noneMatch(s -> s.getSkillId().equals(req.getSkillId()))) continue;
            if (!isAvailableForTimeRange(userId, req.getDate(), req.getStartTime(), req.getEndTime(), input)) continue;
            candidates.add(employee);
        }
        return candidates;
    }

    private List<BreakCoverageRequirement> generateBreakCoverageRequirements(ScheduleMatrix matrix, AlgorithmInput input) {
        List<BreakCoverageRequirement> breakReqs = new ArrayList<>();
        matrix.getAssignments().forEach((key, assignments) -> {
            String[] keyParts = key.split(":");
            Long userId = Long.parseLong(keyParts[0]);
            LocalDate date = LocalDate.parse(keyParts[1]);
            String employeeType = input.getEmployees().stream().filter(e -> e.getUserId().equals(userId)).findFirst().map(HrpUserProfileVo::getEmployeeType).orElse(null);
            if (!"正职".equals(employeeType)) return;
            for (ScheduleAssignment assignment : assignments) {
                List<HrpShiftBreaksVo> breaks = input.getShiftBreaksByShiftId().get(assignment.getShiftId());
                if (breaks != null) {
                    for (HrpShiftBreaksVo breakItem : breaks) {
                        long duration = ChronoUnit.MINUTES.between(breakItem.getBreakStartTime(), breakItem.getBreakEndTime());
                        if (duration >= BREAK_COVERAGE_THRESHOLD_MINUTES) {
                            breakReqs.add(new BreakCoverageRequirement(
                                date,
                                breakItem.getBreakStartTime(),
                                breakItem.getBreakEndTime(),
                                assignment.getSkillId(), userId, false));
                        }
                    }
                }
            }
        });
        return breakReqs;
    }

    private boolean isAvailableForTimeRange(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime, AlgorithmInput input) {
        List<HrpUserAvailabilityVo> availabilities = input.getUserAvailabilities().get(userId);
        if (availabilities == null || availabilities.isEmpty()) return true;
        int dayOfWeek = date.getDayOfWeek().getValue();
        for (HrpUserAvailabilityVo avail : availabilities) {
            if (avail.getDayOfWeek() == dayOfWeek) {
                LocalTime availStart = avail.getStartTime();
                LocalTime availEnd = avail.getEndTime();
                if (!startTime.isBefore(availStart) && !endTime.isAfter(availEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String findEmployeeType(Long userId, List<HrpUserProfileVo> employees) {
        return employees.stream().filter(e -> e.getUserId().equals(userId)).findFirst().map(HrpUserProfileVo::getEmployeeType).orElse("");
    }

    private String findEmployeeName(Long userId, List<HrpUserProfileVo> employees) {
        return employees.stream().filter(e -> e.getUserId().equals(userId)).findFirst().map(HrpUserProfileVo::getUserName).orElse("未知员工");
    }

    @Data
    private static class AlgorithmInput {
        private final LocalDate startDate, endDate;
        private final Long storeId;
        private final List<HrpUserProfileVo> employees;
        private final Map<Long, List<HrpUserSkillsVo>> userSkills;
        private final Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities;
        private final List<HrpLeaveRequestsVo> leaveRequests;
        private final List<HrpShiftsVo> shifts;
        private final Map<Long, HrpShiftsVo> shiftsById;
        private final Map<Long, List<HrpShiftBreaksVo>> shiftBreaksByShiftId;
        private final Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyRequirements;
        private final Map<Long, Set<LocalDate>> pastWorkDays;

        public AlgorithmInput(LocalDate start, LocalDate end, Long storeId, List<HrpUserProfileVo> emp, Map<Long, List<HrpUserSkillsVo>> skills, Map<Long, List<HrpUserAvailabilityVo>> avail, List<HrpLeaveRequestsVo> leaves, List<HrpShiftsVo> shifts, List<HrpShiftBreaksVo> breaks, Map<LocalDate, List<HrpScheduleRequirementsVo>> dailyReqs, Map<Long, Set<LocalDate>> pastWorkDays) {
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
            this.pastWorkDays = pastWorkDays;
        }
    }

    @Data
    @AllArgsConstructor
    private static class ScheduleAssignment {
        private Long shiftId;
        private Long skillId;
    }

    @Data
    @AllArgsConstructor
    private static class BreakAssignment {
        private LocalTime startTime;
        private LocalTime endTime;
        private Long skillId;
        private String remark;
    }

    @Data
    @AllArgsConstructor
    private static class BreakCoverageRequirement {
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long skillId;
        private Long originalUserId;
        private boolean isFulfilled;
    }

    @Data
    private static class ScheduleMatrix {
        private final Map<String, List<ScheduleAssignment>> assignments = new HashMap<>();
        private final Map<String, List<BreakAssignment>> breakCoverageAssignments = new HashMap<>();
        private final Map<String, String> blockedSlots = new HashMap<>();
        private final Map<Long, Double> totalHours;
        private final Map<Long, HrpShiftsVo> shiftsById;
        private final Map<Long, List<HrpShiftBreaksVo>> shiftBreaksByShiftId;
        private final Map<Long, Set<LocalDate>> pastWorkDays;
        private final int maxConsecutiveWorkDays;

        public ScheduleMatrix(LocalDate start, LocalDate end, List<HrpUserProfileVo> employees, Map<Long, HrpShiftsVo> shiftsById, Map<Long, List<HrpShiftBreaksVo>> breaks, Map<Long, Set<LocalDate>> pastWorkDays, int maxConsecutiveWorkDays) {
            this.totalHours = employees.stream().collect(Collectors.toMap(HrpUserProfileVo::getUserId, u -> 0.0));
            this.shiftsById = shiftsById;
            this.shiftBreaksByShiftId = breaks;
            this.pastWorkDays = pastWorkDays;
            this.maxConsecutiveWorkDays = maxConsecutiveWorkDays;
        }

        public void assignShift(Long userId, LocalDate date, Long shiftId, Long skillId) {
            String key = userId + ":" + date;
            assignments.computeIfAbsent(key, k -> new ArrayList<>()).add(new ScheduleAssignment(shiftId, skillId));
            totalHours.merge(userId, getShiftDuration(shiftId), Double::sum);
        }

        public void assignBreakCoverage(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime, Long skillId, String remark) {
            String key = userId + ":" + date;
            breakCoverageAssignments.computeIfAbsent(key, k -> new ArrayList<>()).add(new BreakAssignment(startTime, endTime, skillId, remark));
            long durationMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
            totalHours.merge(userId, durationMinutes / 60.0, Double::sum);
        }

        public boolean isTimeRangeAvailableForBreak(Long userId, LocalDate date, LocalTime newStart, LocalTime newEnd) {
            List<ScheduleAssignment> assignedShifts = assignments.get(userId + ":" + date);
            if (assignedShifts != null) {
                for (ScheduleAssignment assignment : assignedShifts) {
                    HrpShiftsVo existingShift = shiftsById.get(assignment.getShiftId());
                    if (existingShift == null) continue;
                    LocalTime existingStart = existingShift.getStartTime();
                    LocalTime existingEnd = existingShift.getEndTime();
                    if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) return false;
                }
            }
            List<BreakAssignment> assignedBreaks = breakCoverageAssignments.get(userId + ":" + date);
            if (assignedBreaks != null) {
                for (BreakAssignment assignment : assignedBreaks) {
                    if (newStart.isBefore(assignment.getEndTime()) && newEnd.isAfter(assignment.getStartTime())) return false;
                }
            }
            return true;
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
            if (CollectionUtils.isEmpty(assigned)) return true;
            LocalTime newStart = newShift.getStartTime();
            LocalTime newEnd = newShift.getEndTime();
            if (newShift.getIsCrossDay()) newEnd = newEnd.plusHours(24);
            for (ScheduleAssignment assignment : assigned) {
                HrpShiftsVo existingShift = shiftsById.get(assignment.getShiftId());
                if (existingShift == null) continue;
                LocalTime existingStart = existingShift.getStartTime();
                LocalTime existingEnd = existingShift.getEndTime();
                if (existingShift.getIsCrossDay()) existingEnd = existingEnd.plusHours(24);
                if (newStart.isBefore(existingEnd.plusMinutes(1)) && newEnd.isAfter(existingStart.minusMinutes(1))) {
                    return false;
                }
            }
            return true;
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
            Set<LocalDate> userPastWorkDays = pastWorkDays.getOrDefault(userId, Collections.emptySet());
            // 检查过去的天数，直到达到maxConsecutiveWorkDays的限制
            for (int i = 1; i <= this.maxConsecutiveWorkDays + 1; i++) {
                LocalDate date = checkDate.minusDays(i);
                if (assignments.containsKey(userId + ":" + date) || userPastWorkDays.contains(date)) {
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
            for (Map.Entry<String, List<BreakAssignment>> entry : breakCoverageAssignments.entrySet()) {
                if (entry.getKey().startsWith(userId + ":")) {
                    for(BreakAssignment assignment : entry.getValue()){
                        if (assignment.getSkillId().equals(skillId)) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }

        public boolean isValidConsecutiveShift(Long userId, LocalDate date, Long newShiftId) {
            List<ScheduleAssignment> yesterdayAssignmentList = assignments.get(userId + ":" + date.minusDays(1));
            if (yesterdayAssignmentList == null) return true;
            for(ScheduleAssignment yesterdayAssignment : yesterdayAssignmentList){
                HrpShiftsVo yesterdayShift = shiftsById.get(yesterdayAssignment.getShiftId());
                HrpShiftsVo todayShift = shiftsById.get(newShiftId);
                if (yesterdayShift != null && todayShift != null) {
                    if (yesterdayShift.getName().contains("打烊") && todayShift.getName().contains("早")) {
                        return false;
                    }
                }
            }
            return true;
        }

        public double getShiftDuration(Long shiftId) {
            HrpShiftsVo shift = shiftsById.get(shiftId);
            if (shift == null) return 0.0;
            long durationMillis = ChronoUnit.MILLIS.between(shift.getStartTime(), shift.getEndTime());
            if (Boolean.TRUE.equals(shift.getIsCrossDay())) {
                durationMillis += 24 * 60 * 60 * 1000;
            }
            long breakMillis = 0;
            List<HrpShiftBreaksVo> breaks = shiftBreaksByShiftId.get(shiftId);
            if (breaks != null) {
                for (HrpShiftBreaksVo b : breaks) {
                    breakMillis += ChronoUnit.MILLIS.between(b.getBreakStartTime(), b.getBreakEndTime());
                }
            }
            return (durationMillis - breakMillis) / (1000.0 * 60 * 60);
        }

        public Map<String, List<ScheduleAssignment>> getAssignments() {
            return this.assignments;
        }
    }
}
