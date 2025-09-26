package org.dromara.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.wms.domain.WmsShipment;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 出货记录视图对象 wms_shipment
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsShipment.class)
public class WmsShipmentVo extends WmsShipment {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 出货单号
     */
    @ExcelProperty(value = "出货单号")
    private String shipmentNo;

    /**
     * 出货类型(1:分店调货, 2:损耗报废, 3:退货给供应商, 4:领用出库)
     */
    @ExcelProperty(value = "出货类型")
    private Integer shipmentType;

    /**
     * 调出分店ID
     */
    @ExcelProperty(value = "调出分店ID")
    private Long sourceStoreId;

    /**
     * 调入分店ID
     */
    @ExcelProperty(value = "调入分店ID")
    private Long targetStoreId;

    /**
     * 供应商ID
     */
    @ExcelProperty(value = "供应商ID")
    private Long supplierId;

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
     * 领用人名称
     */
    @ExcelProperty(value = "领用人名称")
    private String recipientName;

    /**
     * 出库原因
     */
    @ExcelProperty(value = "出库原因")
    private String reason;

    /**
     * 申请人ID
     */
    @ExcelProperty(value = "申请人ID")
    private Long applicantId;

    /**
     * 出货日期
     */
    @ExcelProperty(value = "出货日期")
    private Date shipmentDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

}