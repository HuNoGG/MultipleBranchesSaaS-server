package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 技能岗位Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpSkillsMapper extends BaseMapperPlus<HrpSkills, HrpSkillsVo> {

    /**
     * 查询用户技能岗位列表
     *
     * @param userId
     * @return
     */
    @Select("select s.* from hrp_skills s " +
            "left join hrp_user_skills us on s.id = us.skill_id " +
            "where us.user_id = #{userId}")
    List<HrpSkillsVo> selectVoListWithUserSkillByUserId(Long userId);
}
