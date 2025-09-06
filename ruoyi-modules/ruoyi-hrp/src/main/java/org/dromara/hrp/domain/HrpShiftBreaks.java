package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 班别休息时段对象 hrp_shift_breaks
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_shift_breaks")
public class HrpShiftBreaks extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 班别 ID
     */
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
