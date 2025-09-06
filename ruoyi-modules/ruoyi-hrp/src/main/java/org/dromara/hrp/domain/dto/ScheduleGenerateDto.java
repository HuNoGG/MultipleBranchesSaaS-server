package org.dromara.hrp.domain.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 排班计划生成 视图传输对象
 * 用于从前端接收生成排班任务所需的所有参数
 * @Author Gemini AI Assistant
 * @Version 2.1
 */
@Data
public class ScheduleGenerateDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "分店ID不能为空")
    private Long storeId;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    /**
     * 新增：排班模式
     * 用于指定算法在选择员工时的策略
     * 可选值: "PRIORITY" (优先模式), "AVERAGE" (平均模式)
     */
    private String schedulingMode;

    /**
     * 参与排班的员工列表及其特殊配置
     */
    private List<EmployeeConfig> employees;

    /**
     * TODO: #2 按天配置的岗位需求
     * 数据结构: Map<"YYYY-MM-DD", Map<"HH:mm-HH:mm", Map<"SkillName", Integer>>>
     * 例如: {"2025-08-25": {"09:00-18:00": {"收银": 2, "出锅": 1}}}
     */
    private Map<String, Map<String, Map<String, Integer>>> requirementsByDay;

    /**
     * TODO: #1 是否为正职员工的休息时段安排替补
     */
    private Boolean enableRestDaySubstitution;

    /**
     * 新增：最大连续工作天数
     * 用于指定员工可以连续工作的最大天数
     * 默认值为 6
     */
    private Integer maxConsecutiveWorkDays;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private List<String> daysOff; // 预设的休息日
    }
}
