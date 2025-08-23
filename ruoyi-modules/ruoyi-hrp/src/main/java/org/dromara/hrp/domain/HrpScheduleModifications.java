package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 排班修改记录对象 hrp_schedule_modifications
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_schedule_modifications")
public class HrpScheduleModifications extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 关联的排班记录ID(原排班表主键)
     */
    private Long scheduleId;

    /**
     * 原员工ID(修改前的排班员工)
     */
    private Long originalUserId;

    /**
     * 新员工ID(修改后的排班员工,change_type为'新增临时')
     */
    private Long newUserId;

    /**
     * 修改类型(字典: 调班, 替班, 新增临时)
     */
    private String changeType;

    /**
     * 修改人ID(操作修改的用户)
     */
    private Long changedBy;

    /**
     * 修改时间
     */
    private Date changeTime;

    /**
     * 备注(如:调班原因、替班说明等)
     */
    private String remark;


}
