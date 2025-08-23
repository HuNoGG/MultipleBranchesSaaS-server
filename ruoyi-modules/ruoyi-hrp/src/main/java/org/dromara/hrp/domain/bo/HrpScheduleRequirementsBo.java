package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpScheduleRequirements;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 每日人力需求业务对象 hrp_schedule_requirements
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpScheduleRequirements.class, reverseConvertGenerate = false)
public class HrpScheduleRequirementsBo extends BaseEntity {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 分店 ID
     */
    @NotNull(message = "分店 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 日期类型(字典: 平日, 假日, 特殊节日)
     */
    @NotBlank(message = "日期类型(字典: 平日, 假日, 特殊节日)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dayType;

    /**
     * 班别 ID
     */
    @NotNull(message = "班别 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shiftId;

    /**
     * 岗位(技能) ID
     */
    @NotNull(message = "岗位(技能) ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skillId;

    /**
     * 需求人数
     */
    private Long requiredCount;

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
