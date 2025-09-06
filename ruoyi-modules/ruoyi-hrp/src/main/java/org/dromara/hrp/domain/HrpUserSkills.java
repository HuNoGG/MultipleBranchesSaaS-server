package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 员工技能关联对象 hrp_user_skills
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@TableName("hrp_user_skills")
public class HrpUserSkills  {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工 ID
     */
//    @TableId(value = "user_id")
    private Long userId;

    /**
     * 技能 ID
     */
//    @TableId(value = "skill_id")
    private Long skillId;

    /**
     *  租户 ID
     */
    private Long tenantId;

    /**
     * 技能优先级 (数字越大, 优先级越高)
     */
    private Long priority;


}
