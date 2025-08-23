package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpUserSkills;
import org.dromara.hrp.domain.vo.HrpUserSkillsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Arrays;
import java.util.List;

/**
 * 员工技能关联Mapper接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface HrpUserSkillsMapper extends BaseMapperPlus<HrpUserSkills, HrpUserSkillsVo> {

    @Select("select * from hrp_user_skills where user_id in (#{userIds}) ")
    List<HrpUserSkillsVo> selectVoListByUserIds(@Param("userIds") List<Long> userIds);
}
