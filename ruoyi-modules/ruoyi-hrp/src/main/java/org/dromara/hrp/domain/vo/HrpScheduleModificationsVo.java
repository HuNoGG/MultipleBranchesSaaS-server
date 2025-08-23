package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpScheduleModifications;
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
 * 排班修改记录视图对象 hrp_schedule_modifications
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpScheduleModifications.class)
public class HrpScheduleModificationsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录 ID
     */
    @ExcelProperty(value = "记录 ID")
    private Long id;

    /**
     * 关联的排班记录ID(原排班表主键)
     */
    @ExcelProperty(value = "关联的排班记录ID(原排班表主键)")
    private Long scheduleId;

    /**
     * 原员工ID(修改前的排班员工)
     */
    @ExcelProperty(value = "原员工ID(修改前的排班员工)")
    private Long originalUserId;

    /**
     * 新员工ID(修改后的排班员工,change_type为'新增临时')
     */
    @ExcelProperty(value = "新员工ID(修改后的排班员工,change_type为'新增临时')")
    private Long newUserId;

    /**
     * 修改类型(字典: 调班, 替班, 新增临时)
     */
    @ExcelProperty(value = "修改类型(字典: 调班, 替班, 新增临时)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_schedule_change_type")
    private String changeType;

    /**
     * 修改人ID(操作修改的用户)
     */
    @ExcelProperty(value = "修改人ID(操作修改的用户)")
    private Long changedBy;

    /**
     * 修改时间
     */
    @ExcelProperty(value = "修改时间")
    private Date changeTime;

    /**
     * 备注(如:调班原因、替班说明等)
     */
    @ExcelProperty(value = "备注(如:调班原因、替班说明等)")
    private String remark;


}
