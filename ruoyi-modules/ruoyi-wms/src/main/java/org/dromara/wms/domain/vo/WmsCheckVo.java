package org.dromara.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.wms.domain.WmsCheck;

import java.io.Serial;
import java.util.Date;

/**
 * 点货视图对象 wms_check
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsCheck.class)
public class WmsCheckVo extends WmsCheck {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @ExcelProperty(value = "唯一ID")
    private Long id;

    /**
     * 点货单号
     */
    @ExcelProperty(value = "点货单号")
    private String checkNo;

    /**
     * 分店ID
     */
    @ExcelProperty(value = "分店ID")
    private Long storeId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 系统账面数量
     */
    @ExcelProperty(value = "系统账面数量")
    private Integer systemQuantity;

    /**
     * 实际盘点数量
     */
    @ExcelProperty(value = "实际盘点数量")
    private Integer actualQuantity;

    /**
     * 差异数量
     */
    @ExcelProperty(value = "差异数量")
    private Integer difference;

    /**
     * 点货日期
     */
    @ExcelProperty(value = "点货日期")
    private Date checkDate;

    /**
     * 状态(0:进行中,1:已完成)
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}