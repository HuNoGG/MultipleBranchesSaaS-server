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
import org.dromara.hrp.domain.bo.HrpStoresBo;
import org.dromara.hrp.domain.vo.HrpStoresVo;
import org.dromara.hrp.domain.HrpStores;
import org.dromara.hrp.mapper.HrpStoresMapper;
import org.dromara.hrp.service.IHrpStoresService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 分店Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpStoresServiceImpl implements IHrpStoresService {

    private final HrpStoresMapper baseMapper;

    /**
     * 查询分店
     *
     * @param id 主键
     * @return 分店
     */
    @Override
    public HrpStoresVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询分店列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分店分页列表
     */
    @Override
    public TableDataInfo<HrpStoresVo> queryPageList(HrpStoresBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpStores> lqw = buildQueryWrapper(bo);
        Page<HrpStoresVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的分店列表
     *
     * @param bo 查询条件
     * @return 分店列表
     */
    @Override
    public List<HrpStoresVo> queryList(HrpStoresBo bo) {
        LambdaQueryWrapper<HrpStores> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpStores> buildQueryWrapper(HrpStoresBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpStores> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpStores::getId);
        lqw.like(StringUtils.isNotBlank(bo.getName()), HrpStores::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), HrpStores::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getCrossDayRule()), HrpStores::getCrossDayRule, bo.getCrossDayRule());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpStores::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增分店
     *
     * @param bo 分店
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpStoresBo bo) {
        HrpStores add = MapstructUtils.convert(bo, HrpStores.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改分店
     *
     * @param bo 分店
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpStoresBo bo) {
        HrpStores update = MapstructUtils.convert(bo, HrpStores.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpStores entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除分店信息
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
}
