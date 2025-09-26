package org.dromara.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.wms.domain.WmsProduct;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 商品信息视图对象 wms_product
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsProduct.class)
public class WmsProductVo extends WmsProduct {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 商品编码
     */
    @ExcelProperty(value = "商品编码")
    private String code;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String name;

    /**
     * 规格
     */
    @ExcelProperty(value = "规格")
    private String specification;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal price;

    /**
     * 状态(1:启用,0:禁用)
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}