package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpAttendanceRecords;
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
 * 打卡记录视图对象 hrp_attendance_records
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpAttendanceRecords.class)
public class HrpAttendanceRecordsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一ID
     */
    @ExcelProperty(value = "记录唯一ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 关联的排班记录ID
     */
    @ExcelProperty(value = "关联的排班记录ID")
    private Long scheduleId;

    /**
     * 上班打卡时间
     */
    @ExcelProperty(value = "上班打卡时间")
    private Date clockInTime;

    /**
     * 下班打卡时间
     */
    @ExcelProperty(value = "下班打卡时间")
    private Date clockOutTime;

    /**
     * 记录日期
     */
    @ExcelProperty(value = "记录日期")
    private Date recordDate;


}
