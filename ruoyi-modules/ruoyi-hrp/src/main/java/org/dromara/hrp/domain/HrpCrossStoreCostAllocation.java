package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 跨店成本分摊设定对象 hrp_cross_store_cost_allocation
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_cross_store_cost_allocation")
public class HrpCrossStoreCostAllocation extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 分摊方式(字典: 依时数, 依比例)
     */
    private String allocationType;

    /**
     * 分摊规则 示例:{"storeA": 50, "storeB":50} (比例分摊)
     */
    private String allocationRules;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
