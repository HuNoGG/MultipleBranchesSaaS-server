package org.dromara.hrp.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 员工档案扩展对象 hrp_user_profile
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_user_profile")
public class HrpUserProfile extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID(关联系统用户表)
     */
//    @TableId(value = "user_id")
    private Long userId;

    /**
     * 员工分类
     */
    private String employeeType;

    /**
     * 分店 ID
     */
    private Long mainStoreId;

    /**
     * 分配工作优先分数
     */
    private Long priorityScore;

    /**
     * 状态(0在职1离职)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
