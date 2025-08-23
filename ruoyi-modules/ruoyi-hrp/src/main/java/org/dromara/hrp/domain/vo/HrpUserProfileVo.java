package org.dromara.hrp.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.hrp.domain.HrpUserProfile;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 员工档案扩展视图对象 hrp_user_profile
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpUserProfile.class)
public class HrpUserProfileVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID(关联系统用户表)
     */
    @ExcelProperty(value = "员工ID(关联系统用户表)")
    private Long userId;

    /**
     * 员工名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "userId", other = "nickName")
    private String userName;

    /**
     * 员工分类
     */
    @ExcelProperty(value = "员工分类", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_employee_type")
    private String employeeType;

    /**
     * 分店 ID
     */
    @ExcelProperty(value = "分店 ID")
    private Long mainStoreId;

    /**
     * 分配工作优先分数
     */
    @ExcelProperty(value = "分配工作优先分数")
    private Long priorityScore;

    /**
     * 状态(0在职1离职)
     */
    @ExcelProperty(value = "状态(0在职1离职)")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 员工技能列表
     */
    private List<HrpSkillsVo> skills;


}
