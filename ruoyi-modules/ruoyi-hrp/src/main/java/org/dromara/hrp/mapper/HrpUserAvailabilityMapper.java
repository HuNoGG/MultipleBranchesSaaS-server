package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpUserAvailability;
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 员工可上班时段Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpUserAvailabilityMapper extends BaseMapperPlus<HrpUserAvailability, HrpUserAvailabilityVo> {


    @Select("select * from hrp_user_availability where user_id in (#{userIds})")
    List<HrpUserAvailabilityVo> selectVoListByUserIds(@Param("userIds") List<Long> userIds);

}
