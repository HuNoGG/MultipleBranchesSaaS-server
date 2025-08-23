package org.dromara.hrp.domain.vo;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpStoreEvents;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 分店特殊事件视图对象 hrp_store_events
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpStoreEvents.class)
public class HrpStoreEventsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    @ExcelProperty(value = "事件ID")
    private Long id;

    /**
     * 分店 ID (关联分店表)
     */
    @ExcelProperty(value = "分店 ID (关联分店表)")
    private Long storeId;

    /**
     * 事件日期
     */
    @ExcelProperty(value = "事件日期")
    private LocalDate eventDate;

    /**
     * 事件类型(字典: 全天放假, 上午放假, 下午放假)
     */
    @ExcelProperty(value = "事件类型(字典: 全天放假, 上午放假, 下午放假)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_store_event_type")
    private String eventType;

    /**
     * 事件描述(如:法定节假日放假、设备检修)
     */
    @ExcelProperty(value = "事件描述(如:法定节假日放假、设备检修)")
    private String description;


}
