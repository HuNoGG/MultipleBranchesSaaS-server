package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpPayrollRecords;
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
 * 薪资单记录业务对象 hrp_payroll_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpPayrollRecords.class, reverseConvertGenerate = false)
public class HrpPayrollRecordsBo extends BaseEntity {

    /**
     * 薪资单 ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 薪资周期开始
     */
    @NotNull(message = "薪资周期开始不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date payPeriodStart;

    /**
     * 薪资周期结束
     */
    @NotNull(message = "薪资周期结束不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date payPeriodEnd;

    /**
     * 总工时
     */
    @NotNull(message = "总工时不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long totalHours;

    /**
     * 基本薪资
     */
    @NotNull(message = "基本薪资不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long basePay;

    /**
     * 加班费
     */
    @NotNull(message = "加班费不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long overtimePay;

    /**
     * 扣款
     */
    @NotNull(message = "扣款不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deductions;

    /**
     * 最终薪资
     */
    @NotNull(message = "最终薪资不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long finalSalary;

    /**
     * 是否已确认(TRUE =是,FALSE = 否)
     */
    @NotNull(message = "是否已确认(TRUE =是,FALSE = 否)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isFinalized;

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
