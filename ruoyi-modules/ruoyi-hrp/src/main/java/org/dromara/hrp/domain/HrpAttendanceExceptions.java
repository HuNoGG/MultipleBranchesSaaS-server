package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 考勤异常对象 hrp_attendance_exceptions
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_attendance_exceptions")
public class HrpAttendanceExceptions extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常记录 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 异常日期
     */
    private Date exceptionDate;

    /**
     * 异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)
     */
    private String exceptionType;

    /**
     * 原始打卡(上班)
     */
    private Date originalClockIn;

    /**
     * 原始打卡(下班)
     */
    private Date originalClockOut;

    /**
     * 修正后打卡(上班)
     */
    private Date correctedClockIn;

    /**
     * 修正后打卡(下班)
     */
    private Date correctedClockOut;

    /**
     * 状态(字典: 待处理, 已修正)
     */
    private String approvalStatus;

    /**
     * 审核人 ID
     */
    private Long approvedBy;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
