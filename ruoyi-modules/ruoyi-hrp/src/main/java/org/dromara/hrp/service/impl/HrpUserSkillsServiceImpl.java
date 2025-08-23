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
import org.dromara.hrp.domain.bo.HrpUserSkillsBo;
import org.dromara.hrp.domain.vo.HrpUserSkillsVo;
import org.dromara.hrp.domain.HrpUserSkills;
import org.dromara.hrp.mapper.HrpUserSkillsMapper;
import org.dromara.hrp.service.IHrpUserSkillsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工技能关联Service业务层处理
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpUserSkillsServiceImpl implements IHrpUserSkillsService {

    private final HrpUserSkillsMapper baseMapper;

    /**
     * 查询员工技能关联
     *
     * @param userId 主键
     * @return 员工技能关联
     */
    @Override
    public HrpUserSkillsVo queryById(Long userId){
        return baseMapper.selectVoById(userId);
    }

    /**
     * 分页查询员工技能关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工技能关联分页列表
     */
    @Override
    public TableDataInfo<HrpUserSkillsVo> queryPageList(HrpUserSkillsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpUserSkills> lqw = buildQueryWrapper(bo);
        Page<HrpUserSkillsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工技能关联列表
     *
     * @param bo 查询条件
     * @return 员工技能关联列表
     */
    @Override
    public List<HrpUserSkillsVo> queryList(HrpUserSkillsBo bo) {
        LambdaQueryWrapper<HrpUserSkills> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpUserSkills> buildQueryWrapper(HrpUserSkillsBo bo) {
        LambdaQueryWrapper<HrpUserSkills> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpUserSkills::getUserId);
        lqw.orderByAsc(HrpUserSkills::getSkillId);
        return lqw;
    }

    /**
     * 新增员工技能关联
     *
     * @param bo 员工技能关联
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpUserSkillsBo bo) {
        HrpUserSkills add = MapstructUtils.convert(bo, HrpUserSkills.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }

    /**
     * 修改员工技能关联
     *
     * @param bo 员工技能关联
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpUserSkillsBo bo) {
        HrpUserSkills update = MapstructUtils.convert(bo, HrpUserSkills.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpUserSkills entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工技能关联信息
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
     * 校验并删除员工技能关联信息
     *
     * @param bo 待删除的业务对象
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteByBo(HrpUserSkillsBo bo) {
        LambdaQueryWrapper<HrpUserSkills> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HrpUserSkills::getUserId, bo.getUserId());
        lqw.eq(HrpUserSkills::getSkillId, bo.getSkillId());
        return baseMapper.delete(lqw) > 0;
    }
}
