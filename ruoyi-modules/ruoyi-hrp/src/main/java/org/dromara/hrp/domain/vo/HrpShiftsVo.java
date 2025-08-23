package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpShifts;
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
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 是否跨日(TRUE =是,FALSE =否)
     */
    @ExcelProperty(value = "是否跨日(TRUE =是,FALSE =否)")
    private Long isCrossDay;

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


}
