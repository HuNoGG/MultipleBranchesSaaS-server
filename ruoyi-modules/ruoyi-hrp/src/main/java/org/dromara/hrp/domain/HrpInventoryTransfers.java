package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 调货记录对象 hrp_inventory_transfers
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_inventory_transfers")
public class HrpInventoryTransfers extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 调货单 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 申请方分店ID (A分店)
     */
    private Long requestStoreId;

    /**
     * 提供方分店ID(B分店)
     */
    private Long providerStoreId;

    /**
     * 申请人ID
     */
    private Long requestUserId;

    /**
     * 调货内容(品项,数量)示例:[{"item":"商品A","qty":10}]
     */
    private String transferDetails;

    /**
     * 状态(字典: 待审核, 待确认, 已完成)
     */
    private String approvalStatus;

    /**
     * 申请方确认时间
     */
    private Date requestConfirmedAt;

    /**
     * 提供方确认时间
     */
    private Date providerConfirmedAt;

    /**
     * 该班次责任人ID
     */
    private Long responsibleUserId;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
