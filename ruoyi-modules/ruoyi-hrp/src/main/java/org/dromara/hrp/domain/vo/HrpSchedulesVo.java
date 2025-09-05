package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpSchedules;
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
 * 排班视图对象 hrp_schedules
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpSchedules.class)
public class HrpSchedulesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排班记录唯一ID
     */
    @ExcelProperty(value = "排班记录唯一ID")
    private Long id;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 上班分店 ID
     */
    @ExcelProperty(value = "上班分店 ID")
    private Long storeId;

    /**
     * 班别 ID
     */
    @ExcelProperty(value = "班别 ID")
    private Long shiftId;

    /**
     * 担任岗位 ID
     */
    @ExcelProperty(value = "担任岗位 ID")
    private Long skillId;

    /**
     * 排班日期
     */
    @ExcelProperty(value = "排班日期")
    private Date scheduleDate;


    /**
     * 排班的发布状态 ('DRAFT', 'PUBLISHED')
     */
    private String status;


    private String version;

    private String userName;
    private String shiftName;
    private String shiftCode;
    private String shiftStartTime;
    private String shiftEndTime;
    private String startTime;
    private String endTime;
    private String shiftColorCode;
    private String colorCode;
    private String skillName;
    private String attendanceStatus;
    private String remark; // 增补对象Remark




}
