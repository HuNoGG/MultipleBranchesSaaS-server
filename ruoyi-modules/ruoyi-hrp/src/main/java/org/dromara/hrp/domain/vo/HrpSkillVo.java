package org.dromara.hrp.domain.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class HrpSkillVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long skillId;
    private String skillName;
}
