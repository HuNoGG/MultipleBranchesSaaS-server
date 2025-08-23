package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpAttendanceExceptions;
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
 * 考勤异常视图对象 hrp_attendance_exceptions
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpAttendanceExceptions.class)
public class HrpAttendanceExceptionsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常记录 ID
     */
    @ExcelProperty(value = "异常记录 ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 异常日期
     */
    @ExcelProperty(value = "异常日期")
    private Date exceptionDate;

    /**
     * 异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)
     */
    @ExcelProperty(value = "异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_attendance_exception_type")
    private String exceptionType;

    /**
     * 原始打卡(上班)
     */
    @ExcelProperty(value = "原始打卡(上班)")
    private Date originalClockIn;

    /**
     * 原始打卡(下班)
     */
    @ExcelProperty(value = "原始打卡(下班)")
    private Date originalClockOut;

    /**
     * 修正后打卡(上班)
     */
    @ExcelProperty(value = "修正后打卡(上班)")
    private Date correctedClockIn;

    /**
     * 修正后打卡(下班)
     */
    @ExcelProperty(value = "修正后打卡(下班)")
    private Date correctedClockOut;

    /**
     * 状态(字典: 待处理, 已修正)
     */
    @ExcelProperty(value = "状态(字典: 待处理, 已修正)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_attendance_approval_status")
    private String approvalStatus;

    /**
     * 审核人 ID
     */
    @ExcelProperty(value = "审核人 ID")
    private Long approvedBy;

    /**
     * 状态(0正常1停用)
     */
    @ExcelProperty(value = "状态(0正常1停用)")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
