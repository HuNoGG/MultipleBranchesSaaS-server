package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpScheduleModifications;
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
 * 排班修改记录业务对象 hrp_schedule_modifications
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpScheduleModifications.class, reverseConvertGenerate = false)
public class HrpScheduleModificationsBo extends BaseEntity {

    /**
     * 记录 ID
     */
    private Long id;

    /**
     * 关联的排班记录ID(原排班表主键)
     */
    @NotNull(message = "关联的排班记录ID(原排班表主键)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long scheduleId;

    /**
     * 原员工ID(修改前的排班员工)
     */
    @NotNull(message = "原员工ID(修改前的排班员工)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long originalUserId;

    /**
     * 新员工ID(修改后的排班员工,change_type为'新增临时')
     */
    @NotNull(message = "新员工ID(修改后的排班员工,change_type为'新增临时')不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long newUserId;

    /**
     * 修改类型(字典: 调班, 替班, 新增临时)
     */
    @NotBlank(message = "修改类型(字典: 调班, 替班, 新增临时)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String changeType;

    /**
     * 修改人ID(操作修改的用户)
     */
    @NotNull(message = "修改人ID(操作修改的用户)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long changedBy;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date changeTime;

    /**
     * 备注(如:调班原因、替班说明等)
     */
    @NotBlank(message = "备注(如:调班原因、替班说明等)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
