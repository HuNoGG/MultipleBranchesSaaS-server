package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsSupplier;
import org.dromara.wms.domain.bo.WmsSupplierBo;
import org.dromara.wms.domain.vo.WmsSupplierVo;
import org.dromara.wms.mapper.WmsSupplierMapper;
import org.dromara.wms.service.IWmsSupplierService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 供应商信息Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsSupplierServiceImpl implements IWmsSupplierService {

    private final WmsSupplierMapper baseMapper;

    /**
     * 查询供应商信息
     */
    @Override
    public WmsSupplierVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询供应商信息列表
     */
    @Override
    public TableDataInfo<WmsSupplierVo> queryPageList(WmsSupplierBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsSupplier> lqw = buildQueryWrapper(bo);
        Page<WmsSupplierVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询供应商信息列表
     */
    @Override
    public List<WmsSupplierVo> queryList(WmsSupplierBo bo) {
        LambdaQueryWrapper<WmsSupplier> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsSupplier> buildQueryWrapper(WmsSupplierBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsSupplier> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), WmsSupplier::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getContactPerson()), WmsSupplier::getContactPerson, bo.getContactPerson());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WmsSupplier::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增供应商信息
     */
    @Override
    public Boolean insertByBo(WmsSupplierBo bo) {
        WmsSupplier add = new WmsSupplier();
        add.setName(bo.getName());
        add.setContactPerson(bo.getContactPerson());
        add.setPhone(bo.getPhone());
        add.setAddress(bo.getAddress());
        add.setStatus(bo.getStatus());
        add.setRemark(bo.getRemark());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改供应商信息
     */
    @Override
    public Boolean updateByBo(WmsSupplierBo bo) {
        WmsSupplier update = new WmsSupplier();
        update.setId(bo.getId());
        update.setName(bo.getName());
        update.setContactPerson(bo.getContactPerson());
        update.setPhone(bo.getPhone());
        update.setAddress(bo.getAddress());
        update.setStatus(bo.getStatus());
        update.setRemark(bo.getRemark());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除供应商信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}