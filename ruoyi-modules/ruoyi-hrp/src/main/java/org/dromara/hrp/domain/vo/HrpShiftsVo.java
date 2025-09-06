package org.dromara.hrp.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.hrp.domain.HrpShifts;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 班别设定视图对象 hrp_shifts
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpShifts.class)
public class HrpShiftsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 班别唯一ID
     */
    @ExcelProperty(value = "班别唯一ID")
    private Long id;

    /**
     * 所属分店 ID
     */
    @ExcelProperty(value = "所属分店 ID")
    private Long storeId;

    /**
     * 班别名称(如:早班)
     */
    @ExcelProperty(value = "班别名称(如:早班)")
    private String name;

    /**
     * 班别代码(如:早)
     */
    @ExcelProperty(value = "班别代码(如:早)")
    private String code;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    /**
     * 是否跨日(TRUE =是,FALSE =否)
     */
    @ExcelProperty(value = "是否跨日(TRUE =是,FALSE =否)")
    private Boolean isCrossDay;

    /**
     * 班表显示颜色(如:#FF5733)
     */
    @ExcelProperty(value = "班表显示颜色(如:#FF5733)")
    private String colorCode;

    /**
     * 状态(0正常1停用)
     */
    @ExcelProperty(value = "状态(0正常1停用)")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 班次休息集合
     */
    private List<HrpShiftBreaksVo> shiftBreaksList;


    /**
     * 新增方法：计算班次时长（小时）
     * @return double 班次的小时数

     */
    public double getDurationInHours() {
        if (startTime == null || endTime == null) {
            return 0.0;
        }
        long durationMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
        // 如果是跨天班次，需要加上24小时
        if (Boolean.TRUE.equals(isCrossDay)) {
            durationMinutes += 24 * 60;
        }
        return durationMinutes / 60.0;
    }

}
