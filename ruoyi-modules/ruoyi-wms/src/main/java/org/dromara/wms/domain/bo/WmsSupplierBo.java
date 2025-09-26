package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsSupplier;

/**
 * 供应商信息业务对象 wms_supplier
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsSupplier.class, reverseConvertGenerate = false)
public class WmsSupplierBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态(1:正常,0:停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}