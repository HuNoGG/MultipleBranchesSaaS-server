package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpPayrollSettings;
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
 * 薪资规则视图对象 hrp_payroll_settings
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpPayrollSettings.class)
public class HrpPayrollSettingsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规则 ID
     */
    @ExcelProperty(value = "规则 ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 月薪(全职适用)
     */
    @ExcelProperty(value = "月薪(全职适用)")
    private Long baseSalary;

    /**
     * 时薪(计时适用)
     */
    @ExcelProperty(value = "时薪(计时适用)")
    private Long hourlyRate;

    /**
     * 生效日期
     */
    @ExcelProperty(value = "生效日期")
    private Date effectiveDate;

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
