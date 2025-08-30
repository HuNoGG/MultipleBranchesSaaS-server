package org.dromara.hrp.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户档案拓展信息更新 DTO
 * 用于接收前端传递的用户基础信息、技能列表和可用时间
 *
 * @author hzy
 * @date 2025-08-23
 */
@Data
public class UserProfileExtendedUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID (hrp_user_profile的主键, 关联sys_user的user_id)
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 员工类型 (例如: '正职', '兼职')
     */
    private String employeeType;

    /**
     * 主要归属店铺ID
     */
    private Long mainStoreId;

    /**
     * 排班优先分数
     */
    private Integer priorityScore;

    /**
     * 状态 (例如: '0' 在职, '1' 离职)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 技能ID列表
     * 前端传递的是用户勾选的所有技能的ID集合
     */
    private List<Long> skills;

    /**
     * 可用时间段列表
     */
    private List<UserAvailabilityDTO> availableTimes;

    /**
     * 内部类, 用于描述可用时间段
     */
    @Data
    public static class UserAvailabilityDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 星期几 (例如: 'MONDAY', 'TUESDAY')
         */
        private String dayOfWeek;
        /**
         * 开始时间 (格式: "HH:mm")
         */
        private String startTime;
        /**
         * 结束时间 (格式: "HH:mm")
         */
        private String endTime;
    }
}
