package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpLeaveApplications;
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
 * 正式请假申请业务对象 hrp_leave_applications
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpLeaveApplications.class, reverseConvertGenerate = false)
public class HrpLeaveApplicationsBo extends BaseEntity {

    /**
     * 申请 ID
     */
    private Long id;

    /**
     * 申请人ID(关联系统用户表)
     */
    @NotNull(message = "申请人ID(关联系统用户表)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 请假类型(如:事假,病假,年假,婚假)
     */
    @NotBlank(message = "请假类型(如:事假,病假,年假,婚假)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String leaveType;

    /**
     * 请假开始时间(精确到时分)
     */
    private Date startTime;

    /**
     * 请假结束时间(精确到时分)
     */
    private Date endTime;

    /**
     * 请假天数(自动计算,支持0.5天粒度)
     */
    @NotNull(message = "请假天数(自动计算,支持0.5天粒度)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long leaveDays;

    /**
     * 请假事由
     */
    @NotBlank(message = "请假事由不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reason;

    /**
     * 附件链接(如病假证明、休假凭证)
     */
    @NotBlank(message = "附件链接(如病假证明、休假凭证)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attachmentUrl;

    /**
     * 审批状态(字典: 待审批, 已批准, 已驳回)
     */
    @NotBlank(message = "审批状态(字典: 待审批, 已批准, 已驳回)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String approvalStatus;

    /**
     * 审批人ID(关联系统用户表,审批后生效)
     */
    @NotNull(message = "审批人ID(关联系统用户表,审批后生效)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long approvedBy;

    /**
     * 数据状态(0正常1停用/作废)
     */
    @NotBlank(message = "数据状态(0正常1停用/作废)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注(如审批意见、特殊说明)
     */
    @NotBlank(message = "备注(如审批意见、特殊说明)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
