package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 排班业务对象 hrp_schedules
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpSchedules.class, reverseConvertGenerate = false)
public class HrpSchedulesBo extends BaseEntity {

    /**
     * 排班记录唯一ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 上班分店 ID
     */
    @NotNull(message = "上班分店 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 班别 ID
     */
    @NotNull(message = "班别 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shiftId;

    /**
     * 担任岗位 ID
     */
    @NotNull(message = "担任岗位 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skillId;

    /**
     * 排班日期
     */
    private LocalDate scheduleDate;


}
