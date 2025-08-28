package org.dromara.hrp.domain.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
     * 动态岗位需求
     * 格式: {"09:00-18:00": {"柜台": 1, "外场": 2}}
     */
    private Map<String, Map<String, Integer>> requirements;

    /**
     * 员工配置内部类
     */
    @Data
    public static class EmployeeConfig {
        private Long id;
        /**
         * 员工的固定休息日
         * 格式: ["星期一 (8.26)", "星期二 (8.27)"]
         */
        private List<String> daysOff;
    }
}
