package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpPayrollSettings;
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
 * 薪资规则业务对象 hrp_payroll_settings
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpPayrollSettings.class, reverseConvertGenerate = false)
public class HrpPayrollSettingsBo extends BaseEntity {

    /**
     * 规则 ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 月薪(全职适用)
     */
    @NotNull(message = "月薪(全职适用)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long baseSalary;

    /**
     * 时薪(计时适用)
     */
    @NotNull(message = "时薪(计时适用)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long hourlyRate;

    /**
     * 生效日期
     */
    @NotNull(message = "生效日期不能为空", groups = { AddGroup.class, EditGroup.class })
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
