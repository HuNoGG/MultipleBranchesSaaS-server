package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsShipment;
import org.dromara.wms.domain.bo.WmsShipmentBo;
import org.dromara.wms.domain.vo.WmsShipmentVo;
import org.dromara.wms.mapper.WmsShipmentMapper;
import org.dromara.wms.service.IWmsShipmentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 出货记录Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsShipmentServiceImpl implements IWmsShipmentService {

    private final WmsShipmentMapper baseMapper;

    /**
     * 查询出货记录
     */
    @Override
    public WmsShipmentVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询出货记录列表
     */
    @Override
    public TableDataInfo<WmsShipmentVo> queryPageList(WmsShipmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsShipment> lqw = buildQueryWrapper(bo);
        Page<WmsShipmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询出货记录列表
     */
    @Override
    public List<WmsShipmentVo> queryList(WmsShipmentBo bo) {
        LambdaQueryWrapper<WmsShipment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsShipment> buildQueryWrapper(WmsShipmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsShipment> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getShipmentNo()), WmsShipment::getShipmentNo, bo.getShipmentNo());
        lqw.eq(bo.getShipmentType() != null, WmsShipment::getShipmentType, bo.getShipmentType());
        lqw.eq(bo.getSourceStoreId() != null, WmsShipment::getSourceStoreId, bo.getSourceStoreId());
        lqw.eq(bo.getProductId() != null, WmsShipment::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WmsShipment::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增出货记录
     */
    @Override
    public Boolean insertByBo(WmsShipmentBo bo) {
        WmsShipment add = new WmsShipment();
        updateAndSave(bo, add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // TODO: 在此集成工作流启动逻辑
        }
        return flag;
    }

    /**
     * 修改出货记录
     */
    @Override
    public Boolean updateByBo(WmsShipmentBo bo) {
        WmsShipment update = new WmsShipment();
        update.setId(bo.getId());
        updateAndSave(bo, update);
        return baseMapper.updateById(update) > 0;
    }

    private void updateAndSave(WmsShipmentBo bo, WmsShipment entity) {
        entity.setShipmentNo(bo.getShipmentNo());
        entity.setShipmentType(bo.getShipmentType());
        entity.setSourceStoreId(bo.getSourceStoreId());
        entity.setTargetStoreId(bo.getTargetStoreId());
        entity.setSupplierId(bo.getSupplierId());
        entity.setProductId(bo.getProductId());
        entity.setQuantity(bo.getQuantity());
        entity.setUnitPrice(bo.getUnitPrice());
        entity.setAmount(bo.getAmount());
        entity.setRecipientName(bo.getRecipientName());
        entity.setReason(bo.getReason());
        entity.setApplicantId(bo.getApplicantId());
        entity.setShipmentDate(bo.getShipmentDate());
        entity.setRemark(bo.getRemark());
        entity.setStatus(bo.getStatus());
    }

    /**
     * 批量删除出货记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO: 检查出库单状态，已确认或审批中的不允许删除
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}