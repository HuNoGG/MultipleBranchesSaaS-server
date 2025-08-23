package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpScheduleRequirements;
import org.dromara.hrp.domain.vo.HrpScheduleRequirementsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 每日人力需求Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpScheduleRequirementsMapper extends BaseMapperPlus<HrpScheduleRequirements, HrpScheduleRequirementsVo> {


    @Select("select * from hrp_schedule_requirements where store_id in (#{storeId})")
    List<HrpScheduleRequirementsVo> selectVoListByStoreId(@Param("storeId") Long storeId);


}
