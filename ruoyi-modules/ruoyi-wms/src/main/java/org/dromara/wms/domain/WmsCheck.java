package org.dromara.wms.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 点货对象 wms_check
 *
 * @author Jules
 * @date 2025-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_check")
public class WmsCheck extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 点货单号
     */
    private String checkNo;

    /**
     * 分店ID
     */
    private Long storeId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 系统账面数量
     */
    private Integer systemQuantity;

    /**
     * 实际盘点数量
     */
    private Integer actualQuantity;

    /**
     * 差异数量
     */
    private Integer difference;

    /**
     * 点货日期
     */
    private Date checkDate;

    /**
     * 状态(0:进行中,1:已完成)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}