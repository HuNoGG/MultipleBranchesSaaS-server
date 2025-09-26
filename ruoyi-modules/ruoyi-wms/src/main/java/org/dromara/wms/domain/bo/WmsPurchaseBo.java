package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsPurchase;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 进货记录业务对象 wms_purchase
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsPurchase.class, reverseConvertGenerate = false)
public class WmsPurchaseBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 进货单号
     */
    @NotBlank(message = "进货单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String purchaseNo;

    /**
     * 供应商ID
     */
    @NotNull(message = "供应商ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long supplierId;

    /**
     * 入库分店ID
     */
    @NotNull(message = "入库分店ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

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
     * 进货日期
     */
    @NotNull(message = "进货日期不能为空", groups = { AddGroup.class, EditGroup.class })
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