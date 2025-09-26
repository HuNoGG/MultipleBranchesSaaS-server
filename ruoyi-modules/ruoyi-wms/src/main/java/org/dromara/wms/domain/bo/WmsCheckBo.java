package org.dromara.wms.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.wms.domain.WmsCheck;

import java.util.Date;

/**
 * 点货业务对象 wms_check
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsCheck.class, reverseConvertGenerate = false)
public class WmsCheckBo extends BaseEntity {

    /**
     * 唯一ID
     */
    @NotNull(message = "唯一ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 点货单号
     */
    @NotBlank(message = "点货单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkNo;

    /**
     * 分店ID
     */
    @NotNull(message = "分店ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 系统账面数量
     */
    @NotNull(message = "系统账面数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer systemQuantity;

    /**
     * 实际盘点数量
     */
    @NotNull(message = "实际盘点数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer actualQuantity;

    /**
     * 差异数量
     */
    @NotNull(message = "差异数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer difference;

    /**
     * 点货日期
     */
    @NotNull(message = "点货日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date checkDate;

    /**
     * 状态(0:进行中,1:已完成)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}