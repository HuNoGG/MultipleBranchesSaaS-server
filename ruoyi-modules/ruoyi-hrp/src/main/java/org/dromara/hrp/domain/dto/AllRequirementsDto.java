package org.dromara.hrp.domain.dto;

import lombok.Data;
import org.dromara.hrp.domain.vo.HrpScheduleRequirementsVo;

import java.io.Serializable;
import java.util.List;

/**
 * 包含所有类型每日人力需求的 DTO
 */
@Data
public class AllRequirementsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<HrpScheduleRequirementsVo> weekday;
    private List<HrpScheduleRequirementsVo> holiday;
    private List<HrpScheduleRequirementsVo> special;
}
