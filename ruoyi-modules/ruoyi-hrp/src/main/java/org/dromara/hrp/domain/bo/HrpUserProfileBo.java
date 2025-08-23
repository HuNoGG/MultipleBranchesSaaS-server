package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 员工档案扩展业务对象 hrp_user_profile
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpUserProfile.class, reverseConvertGenerate = false)
public class HrpUserProfileBo extends BaseEntity {

    /**
     * 员工ID(关联系统用户表)
     */
    private Long userId;

    /**
     * 员工分类
     */
    @NotBlank(message = "员工分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String employeeType;

    /**
     * 分店 ID
     */
    @NotNull(message = "分店 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long mainStoreId;

    /**
     * 分配工作优先分数
     */
    @NotNull(message = "分配工作优先分数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long priorityScore;

    /**
     * 状态(0在职1离职)
     */
    @NotBlank(message = "状态(0在职1离职)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
