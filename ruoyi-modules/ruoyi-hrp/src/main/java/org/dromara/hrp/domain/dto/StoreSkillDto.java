package org.dromara.hrp.domain.dto;

import lombok.Data;
import org.dromara.hrp.domain.vo.HrpSkillsVo;

import java.util.List;

/**
 * @Author If404 Hzy
 * @Date 2025-2025/8/25-14:23
 * @Version 1.0
 */
@Data
public class StoreSkillDto
{
    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 技能岗位集合
     */
    private List<HrpSkillsVo> skills;
}
