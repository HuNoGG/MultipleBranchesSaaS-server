package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpSkills;
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
 * 技能岗位视图对象 hrp_skills
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpSkills.class)
public class HrpSkillsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 技能唯一ID
     */
    @ExcelProperty(value = "技能唯一ID")
    private Long id;

    /**
     * 技能名称(如: 出锅,带位)
     */
    @ExcelProperty(value = "技能名称(如: 出锅,带位)")
    private String name;

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
     * 所属店铺id
     */
    private Long storeId;



}
