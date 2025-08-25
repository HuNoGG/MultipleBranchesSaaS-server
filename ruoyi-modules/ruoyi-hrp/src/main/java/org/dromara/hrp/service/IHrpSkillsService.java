package org.dromara.hrp.service;

import org.dromara.hrp.domain.dto.StoreSkillDto;
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.bo.HrpSkillsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 技能岗位Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpSkillsService {

    /**
     * 查询技能岗位
     *
     * @param id 主键
     * @return 技能岗位
     */
    HrpSkillsVo queryById(Long id);

    /**
     * 分页查询技能岗位列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 技能岗位分页列表
     */
    TableDataInfo<HrpSkillsVo> queryPageList(HrpSkillsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的技能岗位列表
     *
     * @param bo 查询条件
     * @return 技能岗位列表
     */
    List<HrpSkillsVo> queryList(HrpSkillsBo bo);

    /**
     * 新增技能岗位
     *
     * @param bo 技能岗位
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpSkillsBo bo);

    /**
     * 修改技能岗位
     *
     * @param bo 技能岗位
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpSkillsBo bo);

    /**
     * 校验并批量删除技能岗位信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询用户技能岗位列表
     * @param userId
     * @return
     */
    List<HrpSkillsVo> queryListWithUserSkillByUserId(Long userId);

    /**
     * 查询所有门店的技能岗位列表
     * @param bo
     * @return
     */
    List<StoreSkillDto> queryStoreList(HrpSkillsBo bo);
}
