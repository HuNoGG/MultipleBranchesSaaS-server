package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpAttendanceRecords;
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
 * 打卡记录业务对象 hrp_attendance_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpAttendanceRecords.class, reverseConvertGenerate = false)
public class HrpAttendanceRecordsBo extends BaseEntity {

    /**
     * 记录唯一ID
     */
    private Long id;

    /**
     * 员工 ID
     */
    @NotNull(message = "员工 ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 关联的排班记录ID
     */
    @NotNull(message = "关联的排班记录ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long scheduleId;

    /**
     * 上班打卡时间
     */
    @NotNull(message = "上班打卡时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date clockInTime;

    /**
     * 下班打卡时间
     */
    @NotNull(message = "下班打卡时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date clockOutTime;

    /**
     * 记录日期
     */
    @NotNull(message = "记录日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date recordDate;


}
