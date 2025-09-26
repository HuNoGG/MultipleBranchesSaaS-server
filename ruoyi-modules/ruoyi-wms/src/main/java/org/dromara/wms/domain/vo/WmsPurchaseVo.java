package org.dromara.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.wms.domain.WmsPurchase;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 进货记录视图对象 wms_purchase
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsPurchase.class)
public class WmsPurchaseVo extends WmsPurchase {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 进货单号
     */
    @ExcelProperty(value = "进货单号")
    private String purchaseNo;

    /**
     * 供应商ID
     */
    @ExcelProperty(value = "供应商ID")
    private Long supplierId;

    /**
     * 入库分店ID
     */
    @ExcelProperty(value = "入库分店ID")
    private Long storeId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Integer quantity;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private BigDecimal amount;

    /**
     * 进货日期
     */
    @ExcelProperty(value = "进货日期")
    private Date purchaseDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 状态(1:已确认,0:草稿)
     */
    @ExcelProperty(value = "状态")
    private String status;

}