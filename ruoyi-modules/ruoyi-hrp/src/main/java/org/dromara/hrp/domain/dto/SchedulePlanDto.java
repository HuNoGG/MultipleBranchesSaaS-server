package org.dromara.hrp.domain.dto;
import lombok.Data;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 排班计划 视图传输对象
 * 用于向前端返回格式化的排班数据
 */
@Data
public class SchedulePlanDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日期表头
     * e.g. [{ "date": "2023-10-01", "label": "周日" }]
     */
    private List<Map<String, String>> dates;

    /**
     * 参与排班的员工列表
     */
    private List<HrpUserProfileVo> employees;

    /**
     * 排班行数据
     * Key: employeeId
     * Value: { Key: "yyyy-MM-dd", Value: List<HrpSchedulesVo> }
     */
    private Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows;
}
