package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsProduct;

import java.math.BigDecimal;

/**
 * 商品信息业务对象 wms_product
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsProduct.class, reverseConvertGenerate = false)
public class WmsProductBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 规格
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 状态(1:启用,0:禁用)
     */
    private String status;

    /**
     * 扩展字段1
     */
    private String ext1;

    /**
     * 扩展字段2
     */
    private String ext2;

    /**
     * 扩展字段3
     */
    private String ext3;

    /**
     * 备注
     */
    private String remark;

}