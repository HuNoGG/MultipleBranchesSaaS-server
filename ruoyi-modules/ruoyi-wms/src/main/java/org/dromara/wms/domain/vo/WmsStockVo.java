package org.dromara.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.wms.domain.WmsStock;

import java.io.Serial;
import java.util.Date;

/**
 * 库存视图对象 wms_stock
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsStock.class)
public class WmsStockVo extends WmsStock {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 分店ID
     */
    @ExcelProperty(value = "分店ID")
    private Long storeId;

    /**
     * 库存数量
     */
    @ExcelProperty(value = "库存数量")
    private Integer quantity;

    /**
     * 最低库存量
     */
    @ExcelProperty(value = "最低库存量")
    private Integer minQuantity;

    /**
     * 最后更新时间
     */
    @ExcelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

}