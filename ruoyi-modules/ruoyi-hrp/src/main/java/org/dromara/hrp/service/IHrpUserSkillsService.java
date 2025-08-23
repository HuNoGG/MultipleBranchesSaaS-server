package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpUserSkillsVo;
import org.dromara.hrp.domain.bo.HrpUserSkillsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工技能关联Service接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface IHrpUserSkillsService {

    /**
     * 查询员工技能关联
     *
     * @param userId 主键
     * @return 员工技能关联
     */
    HrpUserSkillsVo queryById(Long userId);

    /**
     * 分页查询员工技能关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工技能关联分页列表
     */
    TableDataInfo<HrpUserSkillsVo> queryPageList(HrpUserSkillsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工技能关联列表
     *
     * @param bo 查询条件
     * @return 员工技能关联列表
     */
    List<HrpUserSkillsVo> queryList(HrpUserSkillsBo bo);

    /**
     * 新增员工技能关联
     *
     * @param bo 员工技能关联
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpUserSkillsBo bo);

    /**
     * 修改员工技能关联
     *
     * @param bo 员工技能关联
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpUserSkillsBo bo);

    /**
     * 校验并批量删除员工技能关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
