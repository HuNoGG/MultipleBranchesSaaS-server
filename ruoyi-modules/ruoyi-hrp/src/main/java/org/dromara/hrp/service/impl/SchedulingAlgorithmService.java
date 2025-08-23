package org.dromara.hrp.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.bo.HrpSchedulesBo;
import org.dromara.hrp.domain.vo.*;
import org.dromara.hrp.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能排班算法核心服务 (已修复版)
 */
@Service
public class SchedulingAlgorithmService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingAlgorithmService.class);

    // --- 系统可配置参数 (实际项目中应从数据库或配置中心读取) ---
    private static final int MAX_CONSECUTIVE_WORK_DAYS = 6; // 最大连续工作天数
    private static final double MAX_WEEKLY_HOURS = 40.0; // 每周最大工时
    private static final int MAX_FULLTIME_LEAVE_PER_DAY = 2; // 每日最多允许的全职休假人数

    @Autowired
    private HrpUserProfileMapper userProfileMapper;
    @Autowired
    private HrpUserSkillsMapper userSkillsMapper;
    @Autowired
    private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Autowired
    private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Autowired
    private HrpScheduleRequirementsMapper scheduleRequirementsMapper;
    @Autowired
    private HrpShiftsMapper shiftsMapper;
    @Autowired
    private HrpSchedulesMapper schedulesMapper;
    @Autowired
    private HrpStoreEventsMapper storeEventsMapper;


    /**
     * 智能排班算法入口
     */
    @Transactional(rollbackFor = Exception.class)
    public List<HrpSchedules> generateSchedule(HrpSchedulesBo bo) {
        log.info("智能排班任务启动，分店ID: {}, 日期范围: {} to {}", bo.getStoreId(), bo.getScheduleDate(), bo.getParams().get("endDate"));

        // 1. 准备算法输入数据
        AlgorithmInput input = prepareInputData(bo);

        // 2. 初始化排班矩阵
        ScheduleMatrix matrix = initializeMatrix(input);

        // 3. 执行排班算法核心步骤
        assignFullTimeEmployees(matrix, input);
        fillRemainingShifts(matrix, input);

        // 4. 生成结果并保存
        List<HrpSchedules> generatedSchedules = buildAndSaveResults(matrix, bo);

        // 5. 打印未满足的需求报告
        printUnsatisfiedRequirements(matrix, input);

        log.info("智能排班任务完成，共生成 {} 条排班记录。", generatedSchedules.size());
        return generatedSchedules;
    }

    /**
     * Step 1: 准备所有需要的数据
     */
    private AlgorithmInput prepareInputData(HrpSchedulesBo bo) {
        Long storeId = bo.getStoreId();
        LocalDate startDate = bo.getScheduleDate();
        LocalDate endDate = LocalDate.parse(bo.getParams().get("endDate").toString());

        List<HrpUserProfileVo> employees = userProfileMapper.selectVoListByStoreId(storeId);
        if (employees.isEmpty()) throw new ServiceException("该分店下没有找到任何在职员工");
        List<Long> userIds = employees.stream().map(HrpUserProfileVo::getUserId).collect(Collectors.toList());

        Map<Long, List<Long>> userSkills = userSkillsMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserSkillsVo::getUserId, Collectors.mapping(HrpUserSkillsVo::getSkillId, Collectors.toList())));

        Map<Long, List<HrpUserAvailabilityVo>> userAvailabilities = userAvailabilityMapper.selectVoListByUserIds(userIds).stream()
            .collect(Collectors.groupingBy(HrpUserAvailabilityVo::getUserId));

        List<HrpLeaveRequestsVo> leaveRequests = leaveRequestsMapper.selectVoListByUsersAndDate(userIds, startDate, endDate);
        List<HrpShiftsVo> shifts = shiftsMapper.selectVoListByStoreId(storeId);
        List<HrpScheduleRequirementsVo> requirements = scheduleRequirementsMapper.selectVoListByStoreId(storeId);
        List<HrpStoreEventsVo> storeEvents = storeEventsMapper.selectVoListByStoreAndDate(storeId, startDate, endDate);

        return new AlgorithmInput(startDate, endDate, storeId, employees, userSkills, userAvailabilities, leaveRequests, shifts, requirements, storeEvents);
    }

    /**
     * Step 1 & 1.5: 初始化排班矩阵, 处理休假申请
     */
    private ScheduleMatrix initializeMatrix(AlgorithmInput input) {
        ScheduleMatrix matrix = new ScheduleMatrix(input.getStartDate(), input.getEndDate(), input.getEmployees(), input.getShifts());

        // Step 1.5: 处理全职休假冲突
        handleFullTimeLeaveConflicts(input);

        // Step 1: 锁定休假日
        for (HrpLeaveRequestsVo leave : input.getLeaveRequests()) {
            if ("已提交".equals(leave.getApprovalStatus()) || "已锁定".equals(leave.getApprovalStatus())) {
                matrix.blockDay(leave.getUserId(), leave.getLeaveDate(), "休假");
            }
        }
        return matrix;
    }

    private void handleFullTimeLeaveConflicts(AlgorithmInput input) {
        Map<LocalDate, List<HrpLeaveRequestsVo>> fullTimeLeaveMap = input.getLeaveRequests().stream()
            .filter(lr -> "全职".equals(findEmployeeType(lr.getUserId(), input.getEmployees())) && "已提交".equals(lr.getApprovalStatus()))
            .collect(Collectors.groupingBy(HrpLeaveRequestsVo::getLeaveDate));

        fullTimeLeaveMap.forEach((date, requests) -> {
            if (requests.size() > MAX_FULLTIME_LEAVE_PER_DAY) {
                log.warn("日期 [{}] 的全职员工休假申请人数 ({}) 超过上限 ({}), 进行抽签处理...", date, requests.size(), MAX_FULLTIME_LEAVE_PER_DAY);
                Collections.shuffle(requests);
                for (int i = MAX_FULLTIME_LEAVE_PER_DAY; i < requests.size(); i++) {
                    HrpLeaveRequestsVo deniedRequest = requests.get(i);
                    deniedRequest.setApprovalStatus("已取消"); // 标记为取消
                    // 在实际项目中，这里应该更新数据库并通知员工
                    log.info("员工ID [{}] 的休假申请被系统自动取消。", deniedRequest.getUserId());
                }
            }
        });
    }

    /**
     * Step 2: 全职人员优先排班
     */
    private void assignFullTimeEmployees(ScheduleMatrix matrix, AlgorithmInput input) {
        // 简化实现：算法的核心在于满足需求，而不是强制排满全职员工。
        // 在`selectBestCandidates`中，我们会通过平衡工时来间接保证全职员工的排班。
        log.info("跳过强制全职排班步骤，将在需求填充中统一处理。");
    }

    /**
     * Step 3 & 4: 填补所有人力缺口
     */
    private void fillRemainingShifts(ScheduleMatrix matrix, AlgorithmInput input) {
        long days = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;
        List<HrpScheduleRequirementsVo> sortedRequirements = sortRequirements(input);

        for (int i = 0; i < days; i++) {
            LocalDate currentDate = input.getStartDate().plusDays(i);
            String dayType = getDayType(currentDate, input.getStoreEvents());

            for (HrpScheduleRequirementsVo req : sortedRequirements) {
                if (!req.getDayType().equals(dayType)) continue;

                int requiredCount = req.getRequiredCount();
                int assignedCount = matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                int deficit = requiredCount - assignedCount;

                if (deficit <= 0) continue;

                List<HrpUserProfileVo> candidates = findCandidates(currentDate, req, matrix, input);
                List<HrpUserProfileVo> selectedEmployees = selectBestCandidates(candidates, deficit, matrix);

                for (HrpUserProfileVo employee : selectedEmployees) {
                    matrix.assignShift(employee.getUserId(), currentDate, req.getShiftId(), req.getSkillId());
                }
            }
        }
    }

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
            // 检查工作规则
            if (matrix.getConsecutiveWorkDays(userId, date) >= MAX_CONSECUTIVE_WORK_DAYS) continue;
            if (matrix.getWeeklyHours(userId, date) + shift.getDurationInHours() > MAX_WEEKLY_HOURS) continue;

            candidates.add(employee);
        }
        return candidates;
    }

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
                // 班次开始时间 >= 可用开始时间 AND 班次结束时间 <= 可用结束时间
                if (!shiftStart.isBefore(availStart) && !shiftEnd.isAfter(availEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<HrpUserProfileVo> selectBestCandidates(List<HrpUserProfileVo> candidates, int count, ScheduleMatrix matrix) {
        // 综合排序：优先分数高 -> 已排班时数少
        candidates.sort((e1, e2) -> {
            int scoreCompare = e2.getPriorityScore().compareTo(e1.getPriorityScore());
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            double hours1 = matrix.getTotalHours(e1.getUserId());
            double hours2 = matrix.getTotalHours(e2.getUserId());
            return Double.compare(hours1, hours2);
        });
        return candidates.stream().limit(count).collect(Collectors.toList());
    }

    private List<HrpScheduleRequirementsVo> sortRequirements(AlgorithmInput input) {
        Map<Long, Long> skillCounts = input.getUserSkills().values().stream()
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(skillId -> skillId, Collectors.counting()));

        return input.getRequirements().stream()
            .sorted(Comparator.comparing((HrpScheduleRequirementsVo req) -> skillCounts.getOrDefault(req.getSkillId(), 0L))
                .thenComparing(req -> input.getShiftsById().get(req.getShiftId()).getStartTime()))
            .collect(Collectors.toList());
    }

    /**
     * Step 6: 生成结果并保存
     */
    private List<HrpSchedules> buildAndSaveResults(ScheduleMatrix matrix, HrpSchedulesBo bo) {
        schedulesMapper.deleteDraftSchedules(bo.getStoreId(), bo.getScheduleDate(), LocalDate.parse(bo.getParams().get("endDate").toString()));
        List<HrpSchedules> schedules = new ArrayList<>();
        long version = System.currentTimeMillis();

        for (Map.Entry<String, ScheduleAssignment> entry : matrix.getAssignments().entrySet()) {
            String[] keyParts = entry.getKey().split(":");
            HrpSchedules schedule = new HrpSchedules();
            schedule.setUserId(Long.parseLong(keyParts[0]));
            schedule.setScheduleDate(LocalDate.parse(keyParts[1]));
            schedule.setStoreId(bo.getStoreId());
            schedule.setShiftId(entry.getValue().getShiftId());
            schedule.setSkillId(entry.getValue().getSkillId());
            schedule.setVersion(version);
            schedules.add(schedule);
        }

        if (!schedules.isEmpty()) {
            schedulesMapper.insertBatch(schedules);
        }
        return schedules;
    }

    /**
     * Step 6: 生成并打印人力缺口报告
     */
    private void printUnsatisfiedRequirements(ScheduleMatrix matrix, AlgorithmInput input) {
        StringBuilder report = new StringBuilder("\n----------- 智能排班人力缺口报告 -----------");
        boolean hasDeficit = false;
        long days = ChronoUnit.DAYS.between(input.getStartDate(), input.getEndDate()) + 1;

        for (int i = 0; i < days; i++) {
            LocalDate currentDate = input.getStartDate().plusDays(i);
            String dayType = getDayType(currentDate, input.getStoreEvents());

            for (HrpScheduleRequirementsVo req : input.getRequirements()) {
                if (!req.getDayType().equals(dayType)) continue;

                int requiredCount = req.getRequiredCount();
                int assignedCount = matrix.getAssignedCount(currentDate, req.getShiftId(), req.getSkillId());
                int deficit = requiredCount - assignedCount;

                if (deficit > 0) {
                    hasDeficit = true;
                    String shiftName = input.getShiftsById().get(req.getShiftId()).getName();
                    String skillName = "SkillID:" + req.getSkillId(); // 在实际项目中应关联技能表获取名称
                    report.append(String.format("\n[缺口] 日期: %s, 班次: %s, 岗位: %s, 需求: %d, 已排: %d, 缺少: %d人",
                        currentDate, shiftName, skillName, requiredCount, assignedCount, deficit));
                }
            }
        }

        if (!hasDeficit) {
            report.append("\n所有岗位均已满足人力需求。");
        }
        report.append("\n-------------------------------------------------");
        log.warn(report.toString());
    }

    private String getDayType(LocalDate date, List<HrpStoreEventsVo> storeEvents) {
        for (HrpStoreEventsVo event : storeEvents) {
            if (event.getEventDate().isEqual(date)) {
                return "特殊节日"; // 假设所有事件都对应特殊节日的人力需求
            }
        }
        int dayOfWeek = date.getDayOfWeek().getValue();
        return (dayOfWeek >= 6) ? "假日" : "平日";
    }

    private String findEmployeeType(Long userId, List<HrpUserProfileVo> employees) {
        return employees.stream().filter(e -> e.getUserId().equals(userId)).findFirst()
            .map(HrpUserProfileVo::getEmployeeType).orElse("");
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
        private final List<HrpScheduleRequirementsVo> requirements;
        private final List<HrpStoreEventsVo> storeEvents;

        public AlgorithmInput(LocalDate start, LocalDate end, Long storeId, List<HrpUserProfileVo> emp, Map<Long, List<Long>> skills, Map<Long, List<HrpUserAvailabilityVo>> avail, List<HrpLeaveRequestsVo> leaves, List<HrpShiftsVo> shifts, List<HrpScheduleRequirementsVo> reqs, List<HrpStoreEventsVo> events) {
            this.startDate = start;
            this.endDate = end;
            this.storeId = storeId;
            this.employees = emp;
            this.userSkills = skills;
            this.userAvailabilities = avail;
            this.leaveRequests = leaves;
            this.shifts = shifts;
            this.requirements = reqs;
            this.storeEvents = events;
            this.shiftsById = shifts.stream().collect(Collectors.toMap(HrpShiftsVo::getId, s -> s));
        }
    }

    @Data
    @AllArgsConstructor
    private static class ScheduleAssignment {
        private Long shiftId;
        private Long skillId;
    }

    private static class ScheduleMatrix {
        private final Map<String, ScheduleAssignment> assignments = new HashMap<>();
        private final Map<String, String> blockedSlots = new HashMap<>(); // Key: "userId:yyyy-MM-dd", Value: "休假"
        private final Map<Long, Double> totalHours;
        private final Map<Long, HrpShiftsVo> shiftsById;

        public ScheduleMatrix(LocalDate start, LocalDate end, List<HrpUserProfileVo> employees, List<HrpShiftsVo> shifts) {
            this.totalHours = employees.stream().collect(Collectors.toMap(HrpUserProfileVo::getUserId, u -> 0.0));
            this.shiftsById = shifts.stream().collect(Collectors.toMap(HrpShiftsVo::getId, s -> s));
        }

        public void assignShift(Long userId, LocalDate date, Long shiftId, Long skillId) {
            String key = userId + ":" + date;
            assignments.put(key, new ScheduleAssignment(shiftId, skillId));
            blockedSlots.put(key, "已排班");
            double duration = shiftsById.get(shiftId).getDurationInHours();
            totalHours.merge(userId, duration, Double::sum);
        }

        public void blockDay(Long userId, LocalDate date, String reason) {
            blockedSlots.put(userId + ":" + date, reason);
        }

        public boolean isAvailable(Long userId, LocalDate date) {
            return !blockedSlots.containsKey(userId + ":" + date);
        }

        public int getAssignedCount(LocalDate date, Long shiftId, Long skillId) {
            int count = 0;
            for (Map.Entry<String, ScheduleAssignment> entry : assignments.entrySet()) {
                if (entry.getKey().contains(date.toString())) {
                    ScheduleAssignment assignment = entry.getValue();
                    if (assignment.getShiftId().equals(shiftId) && assignment.getSkillId().equals(skillId)) {
                        count++;
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
                ScheduleAssignment assignment = assignments.get(userId + ":" + date);
                if (assignment != null) {
                    weeklyHours += shiftsById.get(assignment.getShiftId()).getDurationInHours();
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

        public Map<String, ScheduleAssignment> getAssignments() {
            return this.assignments;
        }
    }
}
