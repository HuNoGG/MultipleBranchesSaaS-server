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
import org.dromara.hrp.domain.bo.HrpStoreSupportRelationsBo;
import org.dromara.hrp.domain.vo.HrpStoreSupportRelationsVo;
import org.dromara.hrp.domain.HrpStoreSupportRelations;
import org.dromara.hrp.mapper.HrpStoreSupportRelationsMapper;
import org.dromara.hrp.service.IHrpStoreSupportRelationsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 分店支援关系Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpStoreSupportRelationsServiceImpl implements IHrpStoreSupportRelationsService {

    private final HrpStoreSupportRelationsMapper baseMapper;

    /**
     * 查询分店支援关系
     *
     * @param requestingStoreId 主键
     * @return 分店支援关系
     */
    @Override
    public HrpStoreSupportRelationsVo queryById(Long requestingStoreId){
        return baseMapper.selectVoById(requestingStoreId);
    }

    /**
     * 分页查询分店支援关系列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分店支援关系分页列表
     */
    @Override
    public TableDataInfo<HrpStoreSupportRelationsVo> queryPageList(HrpStoreSupportRelationsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpStoreSupportRelations> lqw = buildQueryWrapper(bo);
        Page<HrpStoreSupportRelationsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的分店支援关系列表
     *
     * @param bo 查询条件
     * @return 分店支援关系列表
     */
    @Override
    public List<HrpStoreSupportRelationsVo> queryList(HrpStoreSupportRelationsBo bo) {
        LambdaQueryWrapper<HrpStoreSupportRelations> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpStoreSupportRelations> buildQueryWrapper(HrpStoreSupportRelationsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpStoreSupportRelations> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpStoreSupportRelations::getRequestingStoreId);
        lqw.orderByAsc(HrpStoreSupportRelations::getSupportingStoreId);
        return lqw;
    }

    /**
     * 新增分店支援关系
     *
     * @param bo 分店支援关系
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpStoreSupportRelationsBo bo) {
        HrpStoreSupportRelations add = MapstructUtils.convert(bo, HrpStoreSupportRelations.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRequestingStoreId(add.getRequestingStoreId());
        }
        return flag;
    }

    /**
     * 修改分店支援关系
     *
     * @param bo 分店支援关系
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpStoreSupportRelationsBo bo) {
        HrpStoreSupportRelations update = MapstructUtils.convert(bo, HrpStoreSupportRelations.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpStoreSupportRelations entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除分店支援关系信息
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
