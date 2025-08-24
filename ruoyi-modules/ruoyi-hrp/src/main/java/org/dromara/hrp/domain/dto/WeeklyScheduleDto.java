package org.dromara.hrp.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;

@Data
public class WeeklyScheduleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排班的发布状态 ('DRAFT', 'PUBLISHED')
     */
    private String status;

    /**
     * 日期表头
     */
    private List<Map<String, String>> dates;

    /**
     * 参与排班的员工列表
     */
    private List<HrpUserProfileVo> employees;

    /**
     * 排班行数据
     */
    private Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows;
}
