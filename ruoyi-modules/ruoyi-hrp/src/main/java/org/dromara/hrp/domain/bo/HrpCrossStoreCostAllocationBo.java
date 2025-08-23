package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpCrossStoreCostAllocation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 跨店成本分摊设定业务对象 hrp_cross_store_cost_allocation
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpCrossStoreCostAllocation.class, reverseConvertGenerate = false)
public class HrpCrossStoreCostAllocationBo extends BaseEntity {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 分摊方式(字典: 依时数, 依比例)
     */
    @NotBlank(message = "分摊方式(字典: 依时数, 依比例)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String allocationType;

    /**
     * 分摊规则 示例:{"storeA": 50, "storeB":50} (比例分摊)
     */
    @NotBlank(message = "分摊规则 不能为空", groups = { AddGroup.class, EditGroup.class })
    private String allocationRules;

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
