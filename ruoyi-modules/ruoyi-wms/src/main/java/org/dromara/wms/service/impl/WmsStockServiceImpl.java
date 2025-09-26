package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsStock;
import org.dromara.wms.domain.bo.WmsStockBo;
import org.dromara.wms.domain.vo.WmsStockVo;
import org.dromara.wms.mapper.WmsStockMapper;
import org.dromara.wms.service.IWmsStockService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Date;

/**
 * 库存Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsStockServiceImpl implements IWmsStockService {

    private final WmsStockMapper baseMapper;

    /**
     * 查询库存
     */
    @Override
    public WmsStockVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询库存列表
     */
    @Override
    public TableDataInfo<WmsStockVo> queryPageList(WmsStockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsStock> lqw = buildQueryWrapper(bo);
        Page<WmsStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询库存列表
     */
    @Override
    public List<WmsStockVo> queryList(WmsStockBo bo) {
        LambdaQueryWrapper<WmsStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsStock> buildQueryWrapper(WmsStockBo bo) {
        LambdaQueryWrapper<WmsStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, WmsStock::getProductId, bo.getProductId());
        lqw.eq(bo.getStoreId() != null, WmsStock::getStoreId, bo.getStoreId());
        return lqw;
    }

    /**
     * 新增库存
     */
    @Override
    public Boolean insertByBo(WmsStockBo bo) {
        WmsStock add = new WmsStock();
        add.setProductId(bo.getProductId());
        add.setStoreId(bo.getStoreId());
        add.setQuantity(bo.getQuantity());
        add.setMinQuantity(bo.getMinQuantity());
        add.setLastUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改库存
     */
    @Override
    public Boolean updateByBo(WmsStockBo bo) {
        WmsStock update = new WmsStock();
        update.setId(bo.getId());
        update.setProductId(bo.getProductId());
        update.setStoreId(bo.getStoreId());
        update.setQuantity(bo.getQuantity());
        update.setMinQuantity(bo.getMinQuantity());
        update.setLastUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除库存
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            // 库存记录通常不直接删除，以保留追溯性。可以考虑逻辑删除或归档。
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}