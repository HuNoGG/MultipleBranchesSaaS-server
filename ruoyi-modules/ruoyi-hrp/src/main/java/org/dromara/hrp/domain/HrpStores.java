package org.dromara.hrp.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 分店对象 hrp_stores
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_stores")
public class HrpStores extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分店唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分店名称
     */
    private String name;

    /**
     * 分店地址
     */
    private String address;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 跨日工时归属规则(字典: by_shift_start, by_calendar_day)
     */
    private String crossDayRule;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
