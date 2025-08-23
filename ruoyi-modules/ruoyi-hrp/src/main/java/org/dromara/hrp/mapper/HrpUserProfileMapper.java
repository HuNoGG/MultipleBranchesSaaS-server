package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;

import java.util.List;

/**
 * 员工档案扩展Mapper接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface HrpUserProfileMapper extends BaseMapperPlus<HrpUserProfile, HrpUserProfileVo>
{ das
    /**
     * 查询员工档案扩展列表
     *
     * @param storeId 分店ID
     * @return 员工档案扩展列表
     */
    @Select("select u.*,usr.user_name as userName from hrp_user_profile u left join sys_user usr on u.user_id = usr.user_id where main_store_id = #{storeId}")
    List<HrpUserProfileVo> selectVoListByStoreId(@Param(="storeId") Long storeId);¥¥
}
yy czx
