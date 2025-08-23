package org.dromara.hrp.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpSkillsBo;
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.mapper.HrpSkillsMapper;
import org.dromara.hrp.service.IHrpSkillsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 技能岗位Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpSkillsServiceImpl implements IHrpSkillsService {

    private final HrpSkillsMapper baseMapper;

    /**
     * 查询技能岗位
     *
     * @param id 主键
     * @return 技能岗位
     */
    @Override
    public HrpSkillsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询技能岗位列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 技能岗位分页列表
     */
    @Override
    public TableDataInfo<HrpSkillsVo> queryPageList(HrpSkillsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpSkills> lqw = buildQueryWrapper(bo);
        Page<HrpSkillsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的技能岗位列表
     *
     * @param bo 查询条件
     * @return 技能岗位列表
     */
    @Override
    public List<HrpSkillsVo> queryList(HrpSkillsBo bo) {
        LambdaQueryWrapper<HrpSkills> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpSkills> buildQueryWrapper(HrpSkillsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpSkills> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpSkills::getId);
        lqw.like(StringUtils.isNotBlank(bo.getName()), HrpSkills::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpSkills::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增技能岗位
     *
     * @param bo 技能岗位
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpSkillsBo bo) {
        HrpSkills add = MapstructUtils.convert(bo, HrpSkills.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改技能岗位
     *
     * @param bo 技能岗位
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpSkillsBo bo) {
        HrpSkills update = MapstructUtils.convert(bo, HrpSkills.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpSkills entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除技能岗位信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 查询用户技能岗位列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<HrpSkillsVo> queryListWithUserSkillByUserId(Long userId) {
        return baseMapper.selectVoListWithUserSkillByUserId(userId);
    }
}
