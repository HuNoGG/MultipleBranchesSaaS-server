package org.dromara.hrp.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 班别设定对象 hrp_shifts
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrp_shifts")
public class HrpShifts extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 班别唯一ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 所属分店 ID
     */
    private Long storeId;

    /**
     * 班别名称(如:早班)
     */
    private String name;

    /**
     * 班别代码(如:早)
     */
    private String code;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 是否跨日(TRUE =是,FALSE =否)
     */
    private Boolean isCrossDay;

    /**
     * 班表显示颜色(如:#FF5733)
     */
    private String colorCode;

    /**
     * 状态(0正常1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
