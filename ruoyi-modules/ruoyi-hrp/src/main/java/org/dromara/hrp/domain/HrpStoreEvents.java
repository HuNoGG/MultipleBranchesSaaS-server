package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 分店特殊事件对象 hrp_store_events
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_store_events")
public class HrpStoreEvents extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分店 ID (关联分店表)
     */
    private Long storeId;

    /**
     * 事件日期
     */
    private Date eventDate;

    /**
     * 事件类型(字典: 全天放假, 上午放假, 下午放假)
     */
    private String eventType;

    /**
     * 事件描述(如:法定节假日放假、设备检修)
     */
    private String description;


}
