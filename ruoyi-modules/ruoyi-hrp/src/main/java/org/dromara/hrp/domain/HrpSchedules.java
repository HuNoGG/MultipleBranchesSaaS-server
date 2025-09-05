package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 排班对象 hrp_schedules
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_schedules")
public class HrpSchedules extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排班记录唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 上班分店 ID
     */
    private Long storeId;

    /**
     * 班别 ID
     */
    private Long shiftId;

    /**
     * 担任岗位 ID
     */
    private Long skillId;

    /**
     * 排班日期
     */
    private LocalDate scheduleDate;
    /**
     * 排班开始时间
     */
    private LocalTime startTime;

    /**
     * 排班结束时间
     */
    private LocalTime endTime;

    /**
     * 版本号,用于管理与回溯
     */
    @Version
    private Integer version;

    /**
     * 排班的发布状态 ('DRAFT', 'PUBLISHED')
     */
    private String status;

    private String remark;



}
