package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpUserStoreAccess;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 员工跨店权限业务对象 hrp_user_store_access
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpUserStoreAccess.class, reverseConvertGenerate = false)
public class HrpUserStoreAccessBo extends BaseEntity {

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 授权支援的分店 ID
     */
    private Long storeId;


}
