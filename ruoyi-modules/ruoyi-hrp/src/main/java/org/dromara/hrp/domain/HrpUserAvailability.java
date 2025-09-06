package org.dromara.hrp.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalTime;

/**
 * 员工可上班时段对象 hrp_user_availability
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_user_availability")
public class HrpUserAvailability extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工 ID
     */
    private Long userId;

    /**
     * 星期几 (1=周一, 2=周二, ..., 7=周日)
     */
    private Integer dayOfWeek;

    /**
     * 可上班的开始时间
     */
    private LocalTime startTime;

    /**
     * 可上班的结束时间
     */
    private LocalTime endTime;


}
