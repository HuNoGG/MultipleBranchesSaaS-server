package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 员工档案扩展Mapper接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface HrpUserProfileMapper extends BaseMapperPlus<HrpUserProfile, HrpUserProfileVo> {

    @Select("select * from hrp_user_profile where main_store_id = #{storeId}")
    List<HrpUserProfileVo> selectVoListByStoreId(@Param("storeId") Long storeId);
}
