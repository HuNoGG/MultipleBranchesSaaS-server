package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpLeaveRequests;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 期望休假/排休记录业务对象 hrp_leave_requests
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpLeaveRequests.class, reverseConvertGenerate = false)
public class HrpLeaveRequestsBo extends BaseEntity {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 期望休假日期
     */
    private Date leaveDate;

    /**
     * 状态(字典: 已提交, 已锁定, 已取消)
     */
    @NotBlank(message = "状态(字典: 已提交, 已锁定, 已取消)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String approvalStatus;

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
