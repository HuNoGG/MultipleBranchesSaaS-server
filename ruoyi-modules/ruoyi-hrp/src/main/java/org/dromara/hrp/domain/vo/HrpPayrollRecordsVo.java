package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpPayrollRecords;
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
 * 薪资单记录视图对象 hrp_payroll_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpPayrollRecords.class)
public class HrpPayrollRecordsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 薪资单 ID
     */
    @ExcelProperty(value = "薪资单 ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 薪资周期开始
     */
    @ExcelProperty(value = "薪资周期开始")
    private Date payPeriodStart;

    /**
     * 薪资周期结束
     */
    @ExcelProperty(value = "薪资周期结束")
    private Date payPeriodEnd;

    /**
     * 总工时
     */
    @ExcelProperty(value = "总工时")
    private Long totalHours;

    /**
     * 基本薪资
     */
    @ExcelProperty(value = "基本薪资")
    private Long basePay;

    /**
     * 加班费
     */
    @ExcelProperty(value = "加班费")
    private Long overtimePay;

    /**
     * 扣款
     */
    @ExcelProperty(value = "扣款")
    private Long deductions;

    /**
     * 最终薪资
     */
    @ExcelProperty(value = "最终薪资")
    private Long finalSalary;

    /**
     * 是否已确认(TRUE =是,FALSE = 否)
     */
    @ExcelProperty(value = "是否已确认(TRUE =是,FALSE = 否)")
    private Long isFinalized;

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
