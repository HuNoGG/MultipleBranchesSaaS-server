package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsStock;

import java.util.Date;

/**
 * 库存业务对象 wms_stock
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsStock.class, reverseConvertGenerate = false)
public class WmsStockBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 分店ID
     */
    @NotNull(message = "分店ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空", groups = { AddGroup.class, EditGroup.class })
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