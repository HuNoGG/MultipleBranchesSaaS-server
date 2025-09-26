package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsPurchase;
import org.dromara.wms.domain.WmsStock;
import org.dromara.wms.domain.bo.WmsPurchaseBo;
import org.dromara.wms.domain.vo.WmsPurchaseVo;
import org.dromara.wms.mapper.WmsPurchaseMapper;
import org.dromara.wms.mapper.WmsStockMapper;
import org.dromara.wms.service.IWmsPurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * 进货记录Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsPurchaseServiceImpl implements IWmsPurchaseService {

    private final WmsPurchaseMapper baseMapper;
    private final WmsStockMapper stockMapper;

    /**
     * 查询进货记录
     */
    @Override
    public WmsPurchaseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询进货记录列表
     */
    @Override
    public TableDataInfo<WmsPurchaseVo> queryPageList(WmsPurchaseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsPurchase> lqw = buildQueryWrapper(bo);
        Page<WmsPurchaseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询进货记录列表
     */
    @Override
    public List<WmsPurchaseVo> queryList(WmsPurchaseBo bo) {
        LambdaQueryWrapper<WmsPurchase> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsPurchase> buildQueryWrapper(WmsPurchaseBo bo) {
        LambdaQueryWrapper<WmsPurchase> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getPurchaseNo()), WmsPurchase::getPurchaseNo, bo.getPurchaseNo());
        lqw.eq(bo.getSupplierId() != null, WmsPurchase::getSupplierId, bo.getSupplierId());
        lqw.eq(bo.getStoreId() != null, WmsPurchase::getStoreId, bo.getStoreId());
        lqw.eq(bo.getProductId() != null, WmsPurchase::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WmsPurchase::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增进货记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(WmsPurchaseBo bo) {
        WmsPurchase add = new WmsPurchase();
        updateAndSave(bo, add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 如果状态是“已确认”，则更新库存
            if ("1".equals(bo.getStatus())) {
                updateStock(bo.getProductId(), bo.getStoreId(), bo.getQuantity());
            }
        }
        return flag;
    }

    /**
     * 修改进货记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(WmsPurchaseBo bo) {
        WmsPurchase oldPurchase = baseMapper.selectById(bo.getId());
        WmsPurchase update = new WmsPurchase();
        update.setId(bo.getId());
        updateAndSave(bo, update);
        boolean flag = baseMapper.updateById(update) > 0;
        // 如果状态从“草稿”变为“已确认”
        if (flag && "0".equals(oldPurchase.getStatus()) && "1".equals(bo.getStatus())) {
            updateStock(bo.getProductId(), bo.getStoreId(), bo.getQuantity());
        }
        return flag;
    }

    private void updateAndSave(WmsPurchaseBo bo, WmsPurchase entity) {
        entity.setPurchaseNo(bo.getPurchaseNo());
        entity.setSupplierId(bo.getSupplierId());
        entity.setStoreId(bo.getStoreId());
        entity.setProductId(bo.getProductId());
        entity.setQuantity(bo.getQuantity());
        entity.setUnitPrice(bo.getUnitPrice());
        entity.setAmount(bo.getAmount());
        entity.setPurchaseDate(bo.getPurchaseDate());
        entity.setRemark(bo.getRemark());
        entity.setStatus(bo.getStatus());
    }

    private void updateStock(Long productId, Long storeId, Integer quantity) {
        LambdaQueryWrapper<WmsStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(WmsStock::getProductId, productId);
        lqw.eq(WmsStock::getStoreId, storeId);
        WmsStock stock = stockMapper.selectOne(lqw);

        if (stock != null) {
            // 更新现有库存
            stock.setQuantity(stock.getQuantity() + quantity);
            stock.setLastUpdateTime(new Date());
            stockMapper.updateById(stock);
        } else {
            // 创建新库存记录
            WmsStock newStock = new WmsStock();
            newStock.setProductId(productId);
            newStock.setStoreId(storeId);
            newStock.setQuantity(quantity);
            newStock.setLastUpdateTime(new Date());
            stockMapper.insert(newStock);
        }
    }

    /**
     * 批量删除进货记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO: 检查采购单状态，已确认的采购单不允许删除
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}