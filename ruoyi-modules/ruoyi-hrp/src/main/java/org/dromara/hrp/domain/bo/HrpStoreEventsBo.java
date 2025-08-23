package org.dromara.hrp.domain.bo;

import org.dromara.hrp.domain.HrpStoreEvents;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 分店特殊事件业务对象 hrp_store_events
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrpStoreEvents.class, reverseConvertGenerate = false)
public class HrpStoreEventsBo extends BaseEntity {

    /**
     * 事件ID
     */
    private Long id;

    /**
     * 分店 ID (关联分店表)
     */
    @NotNull(message = "分店 ID (关联分店表)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long storeId;

    /**
     * 事件日期
     */
    private Date eventDate;

    /**
     * 事件类型(字典: 全天放假, 上午放假, 下午放假)
     */
    @NotBlank(message = "事件类型(字典: 全天放假, 上午放假, 下午放假)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String eventType;

    /**
     * 事件描述(如:法定节假日放假、设备检修)
     */
    @NotBlank(message = "事件描述(如:法定节假日放假、设备检修)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;


}
