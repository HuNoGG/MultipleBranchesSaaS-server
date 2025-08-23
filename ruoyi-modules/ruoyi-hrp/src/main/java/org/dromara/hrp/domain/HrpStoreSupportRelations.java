package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 分店支援关系对象 hrp_store_support_relations
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_store_support_relations")
public class HrpStoreSupportRelations extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求支援的分店ID
     */
//    @TableId(value = "requesting_store_id")
    private Long requestingStoreId;

    /**
     * 可提供支援的分店 ID
     */
//    @TableId(value = "supporting_store_id")
    private Long supportingStoreId;


}
