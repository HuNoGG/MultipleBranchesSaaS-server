package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpUserAvailability;
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
 * 员工可上班时段视图对象 hrp_user_availability
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpUserAvailability.class)
public class HrpUserAvailabilityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 星期几 (1=周一, 2=周二, ..., 7=周日)
     */
    @ExcelProperty(value = "星期几 (1=周一, 2=周二, ..., 7=周日)")
    private Long dayOfWeek;

    /**
     * 可上班的开始时间
     */
    @ExcelProperty(value = "可上班的开始时间")
    private Date startTime;

    /**
     * 可上班的结束时间
     */
    @ExcelProperty(value = "可上班的结束时间")
    private Date endTime;


}
