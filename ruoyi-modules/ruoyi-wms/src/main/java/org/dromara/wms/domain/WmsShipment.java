package org.dromara.wms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出货记录对象 wms_shipment
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment")
public class WmsShipment extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 出货单号
     */
    private String shipmentNo;

    /**
     * 出货类型(1:分店调货, 2:损耗报废, 3:退货给供应商, 4:领用出库)
     */
    private Integer shipmentType;

    /**
     * 调出分店ID(调货时使用)
     */
    private Long sourceStoreId;

    /**
     * 调入分店ID(调货时使用)
     */
    private Long targetStoreId;

    /**
     * 供应商ID(退货时使用)
     */
    private Long supplierId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 领用人名称
     */
    private String recipientName;

    /**
     * 出库原因
     */
    private String reason;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 出货日期
     */
    private Date shipmentDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(1:已确认,0:草稿)
     */
    private String status;

}