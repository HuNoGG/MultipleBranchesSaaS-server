package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 技能岗位对象 hrp_skills
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_skills")
public class HrpSkills extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 技能唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 技能名称(如: 出锅,带位)
     */
    private String name;

    /**
     * 技能描述
     */
    private String description;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属店铺id
     */
    private Long storeId;



}
