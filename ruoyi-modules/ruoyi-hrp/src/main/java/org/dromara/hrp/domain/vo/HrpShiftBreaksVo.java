package org.dromara.hrp.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpShiftBreaks;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * 班别休息时段视图对象 hrp_shift_breaks
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpShiftBreaks.class)
public class HrpShiftBreaksVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 班别 ID
     */
    @ExcelProperty(value = "班别 ID")
    private Long shiftId;

    /**
     * 休息开始时间
     */
    @ExcelProperty(value = "休息开始时间")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStartTime;

    /**
     * 休息结束时间
     */
    @ExcelProperty(value = "休息结束时间")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEndTime;

}
