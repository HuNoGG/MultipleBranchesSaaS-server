package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpStores;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 分店业务对象 hrp_stores
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpStores.class, reverseConvertGenerate = false)
public class HrpStoresBo extends BaseEntity {

    /**
     * 分店唯一ID
     */
    private Long id;

    /**
     * 分店名称
     */
    private String name;

    /**
     * 分店地址
     */
    @NotBlank(message = "分店地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;

    /**
     * 跨日工时归属规则(字典: by_shift_start, by_calendar_day)
     */
    @NotBlank(message = "跨日工时归属规则(字典: by_shift_start, by_calendar_day)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String crossDayRule;

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
