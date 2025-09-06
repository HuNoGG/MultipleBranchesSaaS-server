package org.dromara.hrp.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class HrpUserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userName;
    private String employeeType;
    private Integer priorityScore;
    private Integer workingHours;
    private List<HrpUserSkillsVo> skills;
    private List<HrpUserAvailabilityVo> availableTimes;
    private Long mainStoreId;
}
