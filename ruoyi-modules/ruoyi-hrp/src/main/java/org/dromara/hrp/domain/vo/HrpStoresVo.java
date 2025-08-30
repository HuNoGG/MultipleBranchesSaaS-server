package org.dromara.hrp.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.hrp.domain.HrpStores;
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
 * 分店视图对象 hrp_stores
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpStores.class)
public class HrpStoresVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分店唯一ID
     */
    @ExcelProperty(value = "分店唯一ID")
    private Long id;

    /**
     * 分店名称
     */
    @ExcelProperty(value = "分店名称")
    private String name;

    /**
     * 店长id
     */
    private Long manageId;

    /**
     * 店长名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "manageId")
    private String manageName;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 员工数
     */
    private Integer employeeCount;

    /**
     * 分店地址
     */
    @ExcelProperty(value = "分店地址")
    private String address;

    /**
     * 跨日工时归属规则(字典: by_shift_start, by_calendar_day)
     */
    @ExcelProperty(value = "跨日工时归属规则(字典: by_shift_start, by_calendar_day)")
    private String crossDayRule;

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
