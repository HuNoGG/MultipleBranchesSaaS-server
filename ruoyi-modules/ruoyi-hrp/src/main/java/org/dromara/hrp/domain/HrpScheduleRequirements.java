package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 每日人力需求对象 hrp_schedule_requirements
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_schedule_requirements")
public class HrpScheduleRequirements extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分店 ID
     */
    private Long storeId;

    /**
     * 日期类型(字典: 平日, 假日, 特殊节日)
     */
    private String dayType;

    /**
     * 班别 ID
     */
    private Long shiftId;

    /**
     * 岗位(技能) ID
     */
    private Long skillId;

    /**
     * 需求人数
     */
    private Long requiredCount;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
