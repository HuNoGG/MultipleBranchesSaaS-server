package org.dromara.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.wms.domain.WmsProduct;
import org.dromara.wms.domain.bo.WmsProductBo;
import org.dromara.wms.domain.vo.WmsProductVo;
import org.dromara.wms.mapper.WmsProductMapper;
import org.dromara.wms.service.IWmsProductService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品信息Service业务层处理
 *
 * @author Jules
 * @date 2025-09-26
 */
@RequiredArgsConstructor
@Service
public class WmsProductServiceImpl implements IWmsProductService {

    private final WmsProductMapper baseMapper;

    /**
     * 查询商品信息
     */
    @Override
    public WmsProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品信息列表
     */
    @Override
    public TableDataInfo<WmsProductVo> queryPageList(WmsProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsProduct> lqw = buildQueryWrapper(bo);
        Page<WmsProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品信息列表
     */
    @Override
    public List<WmsProductVo> queryList(WmsProductBo bo) {
        LambdaQueryWrapper<WmsProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsProduct> buildQueryWrapper(WmsProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsProduct> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCode()), WmsProduct::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), WmsProduct::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WmsProduct::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增商品信息
     */
    @Override
    public Boolean insertByBo(WmsProductBo bo) {
        WmsProduct add = new WmsProduct();
        add.setCode(bo.getCode());
        add.setName(bo.getName());
        add.setSpecification(bo.getSpecification());
        add.setUnit(bo.getUnit());
        add.setPrice(bo.getPrice());
        add.setStatus(bo.getStatus());
        add.setRemark(bo.getRemark());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品信息
     */
    @Override
    public Boolean updateByBo(WmsProductBo bo) {
        WmsProduct update = new WmsProduct();
        update.setId(bo.getId());
        update.setCode(bo.getCode());
        update.setName(bo.getName());
        update.setSpecification(bo.getSpecification());
        update.setUnit(bo.getUnit());
        update.setPrice(bo.getPrice());
        update.setStatus(bo.getStatus());
        update.setRemark(bo.getRemark());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除商品信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}