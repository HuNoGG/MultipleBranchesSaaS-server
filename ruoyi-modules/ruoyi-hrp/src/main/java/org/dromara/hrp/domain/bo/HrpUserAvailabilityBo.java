package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpUserAvailability;
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
 * 员工可上班时段业务对象 hrp_user_availability
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpUserAvailability.class, reverseConvertGenerate = false)
public class HrpUserAvailabilityBo extends BaseEntity {

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 星期几 (1=周一, 2=周二, ..., 7=周日)
     */
    @NotNull(message = "星期几 (1=周一, 2=周二, ..., 7=周日)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer dayOfWeek;


    /**
     * 可上班的开始时间
     */
    @NotNull(message = "可上班的开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 可上班的结束时间
     */
    @NotNull(message = "可上班的结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;


}
