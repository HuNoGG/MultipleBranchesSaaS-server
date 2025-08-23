package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpSkills;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 技能岗位业务对象 hrp_skills
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpSkills.class, reverseConvertGenerate = false)
public class HrpSkillsBo extends BaseEntity {

    /**
     * 技能唯一ID
     */
    private Long id;

    /**
     * 技能名称(如: 出锅,带位)
     */
    @NotBlank(message = "技能名称(如: 出锅,带位)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 状态(0正常1停用)
     */
    @NotBlank(message = "状态(0正常1停用)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
