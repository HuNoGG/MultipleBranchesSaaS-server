package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpLeaveApplications;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 正式请假申请视图对象 hrp_leave_applications
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpLeaveApplications.class)
public class HrpLeaveApplicationsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 申请 ID
     */
    @ExcelProperty(value = "申请 ID")
    private Long id;

    /**
     * 申请人ID(关联系统用户表)
     */
    @ExcelProperty(value = "申请人ID(关联系统用户表)")
    private Long userId;

    /**
     * 请假类型(如:事假,病假,年假,婚假)
     */
    @ExcelProperty(value = "请假类型(如:事假,病假,年假,婚假)")
    private String leaveType;

    /**
     * 请假开始时间(精确到时分)
     */
    @ExcelProperty(value = "请假开始时间(精确到时分)")
    private Date startTime;

    /**
     * 请假结束时间(精确到时分)
     */
    @ExcelProperty(value = "请假结束时间(精确到时分)")
    private Date endTime;

    /**
     * 请假天数(自动计算,支持0.5天粒度)
     */
    @ExcelProperty(value = "请假天数(自动计算,支持0.5天粒度)")
    private Long leaveDays;

    /**
     * 请假事由
     */
    @ExcelProperty(value = "请假事由")
    private String reason;

    /**
     * 附件链接(如病假证明、休假凭证)
     */
    @ExcelProperty(value = "附件链接(如病假证明、休假凭证)")
    private String attachmentUrl;

    /**
     * 审批状态(字典: 待审批, 已批准, 已驳回)
     */
    @ExcelProperty(value = "审批状态(字典: 待审批, 已批准, 已驳回)")
    private String approvalStatus;

    /**
     * 审批人ID(关联系统用户表,审批后生效)
     */
    @ExcelProperty(value = "审批人ID(关联系统用户表,审批后生效)")
    private Long approvedBy;

    /**
     * 数据状态(0正常1停用/作废)
     */
    @ExcelProperty(value = "数据状态(0正常1停用/作废)")
    private String status;

    /**
     * 备注(如审批意见、特殊说明)
     */
    @ExcelProperty(value = "备注(如审批意见、特殊说明)")
    private String remark;


}
