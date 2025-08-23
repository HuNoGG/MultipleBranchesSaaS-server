package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 薪资单记录对象 hrp_payroll_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_payroll_records")
public class HrpPayrollRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 薪资单 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 薪资周期开始
     */
    private Date payPeriodStart;

    /**
     * 薪资周期结束
     */
    private Date payPeriodEnd;

    /**
     * 总工时
     */
    private Long totalHours;

    /**
     * 基本薪资
     */
    private Long basePay;

    /**
     * 加班费
     */
    private Long overtimePay;

    /**
     * 扣款
     */
    private Long deductions;

    /**
     * 最终薪资
     */
    private Long finalSalary;

    /**
     * 是否已确认(TRUE =是,FALSE = 否)
     */
    private Long isFinalized;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
