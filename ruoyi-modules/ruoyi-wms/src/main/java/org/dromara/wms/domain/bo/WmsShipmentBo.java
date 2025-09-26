package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsShipment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出货记录业务对象 wms_shipment
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsShipment.class, reverseConvertGenerate = false)
public class WmsShipmentBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 出货单号
     */
    @NotBlank(message = "出货单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String shipmentNo;

    /**
     * 出货类型
     */
    @NotNull(message = "出货类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer shipmentType;

    /**
     * 调出分店ID
     */
    private Long sourceStoreId;

    /**
     * 调入分店ID
     */
    private Long targetStoreId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotNull(message = "出货日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date shipmentDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;

}