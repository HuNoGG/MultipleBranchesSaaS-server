package org.dromara.hrp.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.time.LocalTime;

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
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

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
