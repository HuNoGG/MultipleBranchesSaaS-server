package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 班别休息时段业务对象 hrp_shift_breaks
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpShiftBreaks.class, reverseConvertGenerate = false)
public class HrpShiftBreaksBo extends BaseEntity {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 班别 ID
     */
    @NotNull(message = "班别 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long shiftId;

    /**
     * 休息开始时间
     */
    private LocalTime breakStartTime;

    /**
     * 休息结束时间
     */
    private LocalTime breakEndTime;


}
