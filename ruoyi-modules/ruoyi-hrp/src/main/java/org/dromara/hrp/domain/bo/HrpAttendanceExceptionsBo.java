package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpAttendanceExceptions;
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
 * 考勤异常业务对象 hrp_attendance_exceptions
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpAttendanceExceptions.class, reverseConvertGenerate = false)
public class HrpAttendanceExceptionsBo extends BaseEntity {

    /**
     * 异常记录 ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 异常日期
     */
    private Date exceptionDate;

    /**
     * 异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)
     */
    @NotBlank(message = "异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String exceptionType;

    /**
     * 原始打卡(上班)
     */
    @NotNull(message = "原始打卡(上班)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date originalClockIn;

    /**
     * 原始打卡(下班)
     */
    @NotNull(message = "原始打卡(下班)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date originalClockOut;

    /**
     * 修正后打卡(上班)
     */
    @NotNull(message = "修正后打卡(上班)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date correctedClockIn;

    /**
     * 修正后打卡(下班)
     */
    @NotNull(message = "修正后打卡(下班)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date correctedClockOut;

    /**
     * 状态(字典: 待处理, 已修正)
     */
    @NotBlank(message = "状态(字典: 待处理, 已修正)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String approvalStatus;

    /**
     * 审核人 ID
     */
    @NotNull(message = "审核人 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long approvedBy;

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
