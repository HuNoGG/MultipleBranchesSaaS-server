package org.dromara.hrp.domain.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 排班计划 视图传输对象
 * 用于向前端返回格式化的排班数据
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
     * 参与排班的员工列表
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
        private List<String> daysOff; // 休息日 ["周一", "周二"]
    }
}
