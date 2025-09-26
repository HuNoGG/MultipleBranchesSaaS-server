package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsCheck;
import org.dromara.wms.domain.bo.WmsCheckBo;
import org.dromara.wms.domain.vo.WmsCheckVo;
import org.dromara.wms.mapper.WmsCheckMapper;
import org.dromara.wms.service.IWmsCheckService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 点货Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsCheckServiceImpl implements IWmsCheckService {

    private final WmsCheckMapper baseMapper;

    /**
     * 查询点货
     */
    @Override
    public WmsCheckVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点货列表
     */
    @Override
    public TableDataInfo<WmsCheckVo> queryPageList(WmsCheckBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsCheck> lqw = buildQueryWrapper(bo);
        Page<WmsCheckVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询点货列表
     */
    @Override
    public List<WmsCheckVo> queryList(WmsCheckBo bo) {
        LambdaQueryWrapper<WmsCheck> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsCheck> buildQueryWrapper(WmsCheckBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsCheck> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCheckNo()), WmsCheck::getCheckNo, bo.getCheckNo());
        lqw.eq(bo.getStoreId() != null, WmsCheck::getStoreId, bo.getStoreId());
        lqw.eq(bo.getProductId() != null, WmsCheck::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WmsCheck::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增点货
     */
    @Override
    public Boolean insertByBo(WmsCheckBo bo) {
        WmsCheck add = new WmsCheck();
        updateAndSave(bo, add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改点货
     */
    @Override
    public Boolean updateByBo(WmsCheckBo bo) {
        WmsCheck update = new WmsCheck();
        update.setId(bo.getId());
        updateAndSave(bo, update);
        return baseMapper.updateById(update) > 0;
    }

    private void updateAndSave(WmsCheckBo bo, WmsCheck entity) {
        entity.setCheckNo(bo.getCheckNo());
        entity.setStoreId(bo.getStoreId());
        entity.setProductId(bo.getProductId());
        entity.setSystemQuantity(bo.getSystemQuantity());
        entity.setActualQuantity(bo.getActualQuantity());
        entity.setDifference(bo.getDifference());
        entity.setCheckDate(bo.getCheckDate());
        entity.setStatus(bo.getStatus());
        entity.setRemark(bo.getRemark());
    }

    /**
     * 批量删除点货
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO: 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}