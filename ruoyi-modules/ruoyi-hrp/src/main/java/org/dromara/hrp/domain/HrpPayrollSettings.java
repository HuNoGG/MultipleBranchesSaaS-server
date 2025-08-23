package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 薪资规则对象 hrp_payroll_settings
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_payroll_settings")
public class HrpPayrollSettings extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规则 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 月薪(全职适用)
     */
    private Long baseSalary;

    /**
     * 时薪(计时适用)
     */
    private Long hourlyRate;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
