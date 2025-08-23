package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 打卡记录对象 hrp_attendance_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_attendance_records")
public class HrpAttendanceRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 关联的排班记录ID
     */
    private Long scheduleId;

    /**
     * 上班打卡时间
     */
    private Date clockInTime;

    /**
     * 下班打卡时间
     */
    private Date clockOutTime;

    /**
     * 记录日期
     */
    private Date recordDate;


}
