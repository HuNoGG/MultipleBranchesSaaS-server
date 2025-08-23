package org.dromara.hrp.domain.vo;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpLeaveRequests;
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
 * 期望休假/排休记录视图对象 hrp_leave_requests
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpLeaveRequests.class)
public class HrpLeaveRequestsVo implements Serializable {

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
     * 期望休假日期
     */
    @ExcelProperty(value = "期望休假日期")
    private LocalDate leaveDate;

    /**
     * 状态(字典: 已提交, 已锁定, 已取消)
     */
    @ExcelProperty(value = "状态(字典: 已提交, 已锁定, 已取消)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "common_approval_status")
    private String approvalStatus;

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
