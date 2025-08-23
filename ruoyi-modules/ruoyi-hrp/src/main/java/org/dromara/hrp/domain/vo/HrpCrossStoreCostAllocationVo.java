package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpCrossStoreCostAllocation;
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
 * 跨店成本分摊设定视图对象 hrp_cross_store_cost_allocation
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpCrossStoreCostAllocation.class)
public class HrpCrossStoreCostAllocationVo implements Serializable {

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
     * 分摊方式(字典: 依时数, 依比例)
     */
    @ExcelProperty(value = "分摊方式(字典: 依时数, 依比例)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_cost_allocation_type")
    private String allocationType;

    /**
     * 分摊规则 示例:{"storeA": 50, "storeB":50} (比例分摊)
     */
    @ExcelProperty(value = "分摊规则")
    private String allocationRules;

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
