package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpScheduleRequirements;
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
 * 每日人力需求视图对象 hrp_schedule_requirements
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpScheduleRequirements.class)
public class HrpScheduleRequirementsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 分店 ID
     */
    @ExcelProperty(value = "分店 ID")
    private Long storeId;

    /**
     * 日期类型(字典: 平日, 假日, 特殊节日)
     */
    @ExcelProperty(value = "日期类型(字典: 平日, 假日, 特殊节日)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_schedule_day_type")
    private String dayType;

    /**
     * 班别 ID
     */
    @ExcelProperty(value = "班别 ID")
    private Long shiftId;

    /**
     * 岗位(技能) ID
     */
    @ExcelProperty(value = "岗位(技能) ID")
    private Long skillId;

    /**
     * 需求人数
     */
    @ExcelProperty(value = "需求人数")
    private Integer requiredCount;

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
