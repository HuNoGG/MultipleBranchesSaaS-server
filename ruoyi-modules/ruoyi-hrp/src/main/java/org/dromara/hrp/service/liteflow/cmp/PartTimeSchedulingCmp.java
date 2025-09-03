package org.dromara.hrp.service.liteflow.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.service.liteflow.context.ScheduleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LiteFlow组件 - 兼职员工补充
 * 遍历剩余的排班需求，并使用兼职员工进行填补。
 */
@LiteflowComponent("partTimeSchedulingCmp")
public class PartTimeSchedulingCmp extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(PartTimeSchedulingCmp.class);

    @Override
    public void process() throws Exception {
        ScheduleContext context = this.getContextBean(ScheduleContext.class);
        log.info("【排班流程-阶段二】开始为 {} 名兼职员工安排补位...", context.getPartTimeEmployees().size());

        LocalDate startDate = context.getScheduleGenerateDto().getStartDate();
        LocalDate endDate = context.getScheduleGenerateDto().getEndDate();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Map<Long, Integer> dailyRemaining = context.getRemainingRequirements().get(date);
            if (dailyRemaining == null || dailyRemaining.isEmpty()) continue;

            for (Map.Entry<Long, Integer> reqEntry : dailyRemaining.entrySet()) {
                Long shiftId = reqEntry.getKey();
                while (dailyRemaining.get(shiftId) > 0) {
                    boolean assignedInThisLoop = false;
                    for (HrpUserProfileVo employee : context.getPartTimeEmployees()) {
                        if (checkHardConstraints(context, employee, shiftId, date)) {
                            assignShift(context, employee.getUserId(), shiftId, date);
                            dailyRemaining.put(shiftId, dailyRemaining.get(shiftId) - 1);
                            assignedInThisLoop = true;
                            break;
                        }
                    }
                    if (!assignedInThisLoop) {
                        break; //无法找到兼职填补，避免死循环
                    }
                }
            }
        }
    }

    // 硬性约束检查逻辑 (与全职组件中的方法相同，为了组件独立性在此处复制)
    private boolean checkHardConstraints(ScheduleContext context, HrpUserProfileVo employee, Long shiftId, LocalDate date) {
        if (isEmployeeScheduledOnDate(context, employee.getUserId(), date)) return false;
        if (isEmployeeOnLeave(context, employee.getUserId(), date)) return false;
        if (!isEmployeeAvailable(context, employee, shiftId, date)) return false;
        HrpShiftsVo shift = context.getShiftsById().get(shiftId);
        if (shift == null) return false;
        if (shift.getSkillId() != null && !hasSkill(context, employee.getUserId(), shift.getSkillId())) return false;
        return true;
    }

    private void assignShift(ScheduleContext context, Long employeeId, Long shiftId, LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        context.getAssignments().get(dateStr).add(new ScheduleContext.ScheduleAssignment(employeeId, shiftId));
        ScheduleContext.EmployeeWorkload workload = context.getEmployeeWorkloadTracker().get(employeeId);
        if (workload != null) {
            workload.incrementWorkDays();
        }
    }

    private boolean isEmployeeScheduledOnDate(ScheduleContext context, Long employeeId, LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<ScheduleContext.ScheduleAssignment> dayAssignments = context.getAssignments().get(dateStr);
        return dayAssignments != null && dayAssignments.stream().anyMatch(a -> a.getEmployeeId().equals(employeeId));
    }

    private boolean isEmployeeOnLeave(ScheduleContext context, Long employeeId, LocalDate date) {
        return context.getLeaveByEmployeeAndDate().containsKey(employeeId + "_" + date);
    }

    private boolean isEmployeeAvailable(ScheduleContext context, HrpUserProfileVo employee, Long shiftId, LocalDate date) {
        String key = employee.getUserId() + "_" + date.toString();
        List<HrpUserAvailabilityVo> availabilities = context.getAvailabilityByEmployeeAndDate().get(key);

        if ("2".equals(String.valueOf(employee.getEmploymentType())) && CollectionUtils.isEmpty(availabilities)) {
            return true;
        }
        if (CollectionUtils.isEmpty(availabilities)) {
            return false;
        }

        HrpShiftsVo shift = context.getShiftsById().get(shiftId);
        if (shift == null) return false;

        LocalTime shiftStart = shift.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime shiftEnd = shift.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

        for (HrpUserAvailabilityVo availability : availabilities) {
            LocalTime availableStart = availability.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime availableEnd = availability.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (!shiftStart.isBefore(availableStart) && !shiftEnd.isAfter(availableEnd)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSkill(ScheduleContext context, Long employeeId, Long requiredSkillId) {
        Set<Long> employeeSkills = context.getSkillsByEmployee().get(employeeId);
        return employeeSkills != null && employeeSkills.contains(requiredSkillId);
    }
}
