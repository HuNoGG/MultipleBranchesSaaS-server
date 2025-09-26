package org.dromara.wms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 进货记录对象 wms_purchase
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_purchase")
public class WmsPurchase extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 进货单号
     */
    private String purchaseNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 入库分店ID
     */
    private Long storeId;

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
     * 进货日期
     */
    private Date purchaseDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(1:已确认,0:草稿)
     */
    private String status;

}