package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpInventoryTransfers;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 调货记录业务对象 hrp_inventory_transfers
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpInventoryTransfers.class, reverseConvertGenerate = false)
public class HrpInventoryTransfersBo extends BaseEntity {

    /**
     * 调货单 ID
     */
    private Long id;

    /**
     * 申请方分店ID (A分店)
     */
    @NotNull(message = "申请方分店ID (A分店)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long requestStoreId;

    /**
     * 提供方分店ID(B分店)
     */
    @NotNull(message = "提供方分店ID(B分店)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long providerStoreId;

    /**
     * 申请人ID
     */
    @NotNull(message = "申请人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long requestUserId;

    /**
     * 调货内容(品项,数量)示例:[{"item":"商品A","qty":10}]
     */
    @NotBlank(message = "调货内容(品项,数量)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transferDetails;

    /**
     * 状态(字典: 待审核, 待确认, 已完成)
     */
    @NotBlank(message = "状态(字典: 待审核, 待确认, 已完成)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String approvalStatus;

    /**
     * 申请方确认时间
     */
    @NotNull(message = "申请方确认时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date requestConfirmedAt;

    /**
     * 提供方确认时间
     */
    @NotNull(message = "提供方确认时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date providerConfirmedAt;

    /**
     * 该班次责任人ID
     */
    @NotNull(message = "该班次责任人ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long responsibleUserId;

    /**
     * 状态(0正常1停用)
     */
    @NotBlank(message = "状态(0正常1停用)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
