package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpUserSkills;
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
 * 员工技能关联视图对象 hrp_user_skills
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpUserSkills.class)
public class HrpUserSkillsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 技能 ID
     */
    @ExcelProperty(value = "技能 ID")
    private Long skillId;

    /**
     * 技能优先级 (数字越大, 优先级越高)
     */
    @ExcelProperty(value = "技能优先级")
    private Long priority;


}
