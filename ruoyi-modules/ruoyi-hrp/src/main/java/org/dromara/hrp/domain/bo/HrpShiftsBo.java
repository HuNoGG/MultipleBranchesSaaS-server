package org.dromara.hrp.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.hrp.domain.HrpShifts;

import java.time.LocalTime;

/**
 * 班别设定业务对象 hrp_shifts
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpShifts.class, reverseConvertGenerate = false)
public class HrpShiftsBo extends BaseEntity {

    /**
     * 班别唯一ID
     */
    private Long id;

    /**
     * 所属分店 ID
     */
    @NotNull(message = "所属分店 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 班别名称(如:早班)
     */
    private String name;

    /**
     * 班别代码(如:早)
     */
    @NotBlank(message = "班别代码(如:早)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 是否跨日(TRUE =是,FALSE =否)
     */
    @NotNull(message = "是否跨日(TRUE =是,FALSE =否)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Boolean isCrossDay;

    /**
     * 班表显示颜色(如:#FF5733)
     */
    @NotBlank(message = "班表显示颜色(如:#FF5733)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String colorCode;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
