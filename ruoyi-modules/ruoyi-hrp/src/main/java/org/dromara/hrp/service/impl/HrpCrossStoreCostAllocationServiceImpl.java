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
import org.dromara.hrp.domain.bo.HrpCrossStoreCostAllocationBo;
import org.dromara.hrp.domain.vo.HrpCrossStoreCostAllocationVo;
import org.dromara.hrp.domain.HrpCrossStoreCostAllocation;
import org.dromara.hrp.mapper.HrpCrossStoreCostAllocationMapper;
import org.dromara.hrp.service.IHrpCrossStoreCostAllocationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 跨店成本分摊设定Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpCrossStoreCostAllocationServiceImpl implements IHrpCrossStoreCostAllocationService {

    private final HrpCrossStoreCostAllocationMapper baseMapper;

    /**
     * 查询跨店成本分摊设定
     *
     * @param id 主键
     * @return 跨店成本分摊设定
     */
    @Override
    public HrpCrossStoreCostAllocationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询跨店成本分摊设定列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 跨店成本分摊设定分页列表
     */
    @Override
    public TableDataInfo<HrpCrossStoreCostAllocationVo> queryPageList(HrpCrossStoreCostAllocationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpCrossStoreCostAllocation> lqw = buildQueryWrapper(bo);
        Page<HrpCrossStoreCostAllocationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的跨店成本分摊设定列表
     *
     * @param bo 查询条件
     * @return 跨店成本分摊设定列表
     */
    @Override
    public List<HrpCrossStoreCostAllocationVo> queryList(HrpCrossStoreCostAllocationBo bo) {
        LambdaQueryWrapper<HrpCrossStoreCostAllocation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpCrossStoreCostAllocation> buildQueryWrapper(HrpCrossStoreCostAllocationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpCrossStoreCostAllocation> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpCrossStoreCostAllocation::getId);
        lqw.eq(bo.getUserId() != null, HrpCrossStoreCostAllocation::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getAllocationType()), HrpCrossStoreCostAllocation::getAllocationType, bo.getAllocationType());
        lqw.eq(StringUtils.isNotBlank(bo.getAllocationRules()), HrpCrossStoreCostAllocation::getAllocationRules, bo.getAllocationRules());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpCrossStoreCostAllocation::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增跨店成本分摊设定
     *
     * @param bo 跨店成本分摊设定
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpCrossStoreCostAllocationBo bo) {
        HrpCrossStoreCostAllocation add = MapstructUtils.convert(bo, HrpCrossStoreCostAllocation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改跨店成本分摊设定
     *
     * @param bo 跨店成本分摊设定
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpCrossStoreCostAllocationBo bo) {
        HrpCrossStoreCostAllocation update = MapstructUtils.convert(bo, HrpCrossStoreCostAllocation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpCrossStoreCostAllocation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除跨店成本分摊设定信息
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
