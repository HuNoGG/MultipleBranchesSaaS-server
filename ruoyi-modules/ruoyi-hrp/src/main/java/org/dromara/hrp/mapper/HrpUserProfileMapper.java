package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工档案扩展Mapper接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface HrpUserProfileMapper extends BaseMapperPlus<HrpUserProfile, HrpUserProfileVo>
{     /**
     * 查询员工档案扩展列表
     *
     * @param storeId 分店ID
     * @return 员工档案扩展列表
     */
    @Select("select u.*,usr.user_name as userName from hrp_user_profile u left join sys_user usr on u.user_id = usr.user_id where main_store_id = #{storeId}")
    List<HrpUserProfileVo> selectVoListByStoreId(@Param("storeId") Long storeId);

    /**
     * 查询员工档案扩展列表
     *
     * @param userIds 员工ID
     * @return 员工档案扩展列表
     */
    List<HrpUserProfileVo> selectVoByUserIds(@Param("userIds") List<Long> userIds);


    /**
     * 查询可用的代班员工列表
     * @param storeId 门店ID
     * @param skillId 必需的技能ID
     * @param scheduleDate 排班日期
     * @param originalUserId 被代班的员工ID
     * @return 可用员工列表
     */
    List<HrpUserProfileVo> selectAvailableSubstitutes(@Param("storeId") Long storeId,
        @Param("skillId") Long skillId,
        @Param("scheduleDate") LocalDate scheduleDate,
        @Param("originalUserId") Long originalUserId);


}

