package org.dromara.hrp.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrp.domain.HrpInventoryTransfers;
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
 * 调货记录视图对象 hrp_inventory_transfers
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpInventoryTransfers.class)
public class HrpInventoryTransfersVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 调货单 ID
     */
    @ExcelProperty(value = "调货单 ID")
    private Long id;

    /**
     * 申请方分店ID (A分店)
     */
    @ExcelProperty(value = "申请方分店ID (A分店)")
    private Long requestStoreId;

    /**
     * 提供方分店ID(B分店)
     */
    @ExcelProperty(value = "提供方分店ID(B分店)")
    private Long providerStoreId;

    /**
     * 申请人ID
     */
    @ExcelProperty(value = "申请人ID")
    private Long requestUserId;

    /**
     * 调货内容(品项,数量)示例:[{"item":"商品A","qty":10}]
     */
    @ExcelProperty(value = "调货内容(品项,数量)")
    private String transferDetails;

    /**
     * 状态(字典: 待审核, 待确认, 已完成)
     */
    @ExcelProperty(value = "状态(字典: 待审核, 待确认, 已完成)", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "hrp_inventory_transfer_status")
    private String approvalStatus;

    /**
     * 申请方确认时间
     */
    @ExcelProperty(value = "申请方确认时间")
    private Date requestConfirmedAt;

    /**
     * 提供方确认时间
     */
    @ExcelProperty(value = "提供方确认时间")
    private Date providerConfirmedAt;

    /**
     * 该班次责任人ID
     */
    @ExcelProperty(value = "该班次责任人ID")
    private Long responsibleUserId;

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
