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


    /**
     * 查询员工可上班时段列表
     *
     * @param userIds 用户ID列表
     * @return 员工可上班时段列表
     */
    List<HrpUserAvailabilityVo> selectVoListByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 根据用户ID查询员工可上班时段列表
     *
     * @param userId 用户ID
     * @return 员工可上班时段列表
     */
    @Select("select * from hrp_user_availability where user_id = #{userId}")
    List<HrpUserAvailabilityVo> queryListWithUserAvailabilityByUserId(@Param("userId") Long userId);
}
