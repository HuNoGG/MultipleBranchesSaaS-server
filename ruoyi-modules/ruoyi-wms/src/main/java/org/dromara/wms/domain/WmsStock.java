package org.dromara.wms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 库存对象 wms_stock
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_stock")
public class WmsStock extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 分店ID
     */
    private Long storeId;

    /**
     * 库存数量
     */
    private Integer quantity;

    /**
     * 最低库存量
     */
    private Integer minQuantity;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

}