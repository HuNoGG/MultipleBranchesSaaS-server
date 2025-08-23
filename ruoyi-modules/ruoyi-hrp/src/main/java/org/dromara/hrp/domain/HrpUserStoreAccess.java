package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 员工跨店权限对象 hrp_user_store_access
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_user_store_access")
public class HrpUserStoreAccess extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工 ID
     */
//    @TableId(value = "user_id")
    private Long userId;

    /**
     * 授权支援的分店 ID
     */
//    @TableId(value = "store_id")
    private Long storeId;


}
