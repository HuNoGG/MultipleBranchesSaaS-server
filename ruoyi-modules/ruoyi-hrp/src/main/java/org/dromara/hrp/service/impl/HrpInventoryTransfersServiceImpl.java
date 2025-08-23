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
import org.dromara.hrp.domain.bo.HrpInventoryTransfersBo;
import org.dromara.hrp.domain.vo.HrpInventoryTransfersVo;
import org.dromara.hrp.domain.HrpInventoryTransfers;
import org.dromara.hrp.mapper.HrpInventoryTransfersMapper;
import org.dromara.hrp.service.IHrpInventoryTransfersService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 调货记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpInventoryTransfersServiceImpl implements IHrpInventoryTransfersService {

    private final HrpInventoryTransfersMapper baseMapper;

    /**
     * 查询调货记录
     *
     * @param id 主键
     * @return 调货记录
     */
    @Override
    public HrpInventoryTransfersVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询调货记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 调货记录分页列表
     */
    @Override
    public TableDataInfo<HrpInventoryTransfersVo> queryPageList(HrpInventoryTransfersBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpInventoryTransfers> lqw = buildQueryWrapper(bo);
        Page<HrpInventoryTransfersVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的调货记录列表
     *
     * @param bo 查询条件
     * @return 调货记录列表
     */
    @Override
    public List<HrpInventoryTransfersVo> queryList(HrpInventoryTransfersBo bo) {
        LambdaQueryWrapper<HrpInventoryTransfers> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpInventoryTransfers> buildQueryWrapper(HrpInventoryTransfersBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpInventoryTransfers> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpInventoryTransfers::getId);
        lqw.eq(bo.getRequestStoreId() != null, HrpInventoryTransfers::getRequestStoreId, bo.getRequestStoreId());
        lqw.eq(bo.getProviderStoreId() != null, HrpInventoryTransfers::getProviderStoreId, bo.getProviderStoreId());
        lqw.eq(bo.getRequestUserId() != null, HrpInventoryTransfers::getRequestUserId, bo.getRequestUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getTransferDetails()), HrpInventoryTransfers::getTransferDetails, bo.getTransferDetails());
        lqw.eq(StringUtils.isNotBlank(bo.getApprovalStatus()), HrpInventoryTransfers::getApprovalStatus, bo.getApprovalStatus());
        lqw.eq(bo.getRequestConfirmedAt() != null, HrpInventoryTransfers::getRequestConfirmedAt, bo.getRequestConfirmedAt());
        lqw.eq(bo.getProviderConfirmedAt() != null, HrpInventoryTransfers::getProviderConfirmedAt, bo.getProviderConfirmedAt());
        lqw.eq(bo.getResponsibleUserId() != null, HrpInventoryTransfers::getResponsibleUserId, bo.getResponsibleUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpInventoryTransfers::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增调货记录
     *
     * @param bo 调货记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpInventoryTransfersBo bo) {
        HrpInventoryTransfers add = MapstructUtils.convert(bo, HrpInventoryTransfers.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改调货记录
     *
     * @param bo 调货记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpInventoryTransfersBo bo) {
        HrpInventoryTransfers update = MapstructUtils.convert(bo, HrpInventoryTransfers.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpInventoryTransfers entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除调货记录信息
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
