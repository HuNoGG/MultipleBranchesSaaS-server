package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpUserSkills;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 员工技能关联业务对象 hrp_user_skills
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@AutoMapper(target = HrpUserSkills.class, reverseConvertGenerate = false)
public class HrpUserSkillsBo {

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 技能 ID
     */
    private Long skillId;


}
