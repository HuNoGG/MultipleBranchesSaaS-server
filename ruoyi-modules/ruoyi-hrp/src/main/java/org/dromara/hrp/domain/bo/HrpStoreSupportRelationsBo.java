package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpStoreSupportRelations;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 分店支援关系业务对象 hrp_store_support_relations
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpStoreSupportRelations.class, reverseConvertGenerate = false)
public class HrpStoreSupportRelationsBo extends BaseEntity {

    /**
     * 请求支援的分店ID
     */
    private Long requestingStoreId;

    /**
     * 可提供支援的分店 ID
     */
    private Long supportingStoreId;


}
