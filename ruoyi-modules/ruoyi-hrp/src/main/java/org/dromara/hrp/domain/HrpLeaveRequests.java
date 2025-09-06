package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 期望休假/排休记录对象 hrp_leave_requests
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_leave_requests")
public class HrpLeaveRequests extends BaseEntity {

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
     * 期望休假日期
     */
    private LocalDate leaveDate;

    /**
     * 状态(字典: 已提交, 已锁定, 已取消)
     */
    private String approvalStatus;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
