package org.dromara.hrp.service.liteflow.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.vo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LiteFlow排班流程上下文对象
 * 用于在各个组件之间传递数据
 */
@Data
public class ScheduleContext {

    // --- 输入参数 ---
    private ScheduleGenerateDto scheduleGenerateDto;

    // --- 数据准备阶段加载的数据 ---
    private List<HrpUserProfileVo> allEmployees;
    private List<HrpShiftsVo> shifts;
    private List<HrpScheduleRequirementsVo> requirements;
    private List<HrpUserAvailabilityVo> availabilities;
    private List<HrpLeaveRequestsVo> leaveRequests;
    private List<HrpUserSkillsVo> userSkills;

    // --- 预处理后的快速查找Map ---
    private Map<Long, HrpShiftsVo> shiftsById;
    private Map<String, List<HrpUserAvailabilityVo>> availabilityByEmployeeAndDate;
    private Map<String, HrpLeaveRequestsVo> leaveByEmployeeAndDate;
    private Map<Long, Set<Long>> skillsByEmployee;

    // --- 算法核心状态 ---
    private List<HrpUserProfileVo> fullTimeEmployees;
    private List<HrpUserProfileVo> partTimeEmployees;
    private Map<LocalDate, Map<Long, Integer>> remainingRequirements;
    private Map<String, List<ScheduleAssignment>> assignments; // 最终排班结果
    private Map<Long, EmployeeWorkload> employeeWorkloadTracker;


    /**
     * 用于存储最终排班结果的数据结构
     */
    @Data
    @AllArgsConstructor
    public static class ScheduleAssignment {
        private Long employeeId;
        private Long shiftId;
    }

    /**
     * 用于跟踪员工工作负载的数据结构
     */
    @Data
    @NoArgsConstructor
    public static class EmployeeWorkload {
        private Long employeeId;
        private int totalWorkDays;
        private double totalWorkHours;

        public EmployeeWorkload(Long employeeId) {
            this.employeeId = employeeId;
            this.totalWorkDays = 0;
            this.totalWorkHours = 0.0;
        }

        public void incrementWorkDays() {
            this.totalWorkDays++;
        }

        public void addWorkHours(double hours) {
            this.totalWorkHours += hours;
        }
    }
}
