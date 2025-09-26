package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsSupplierVo;
import org.dromara.wms.domain.bo.WmsSupplierBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 供应商信息Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsSupplierService {

    /**
     * 查询供应商信息
     */
    WmsSupplierVo queryById(Long id);

    /**
     * 查询供应商信息列表
     */
    TableDataInfo<WmsSupplierVo> queryPageList(WmsSupplierBo bo, PageQuery pageQuery);

    /**
     * 查询供应商信息列表
     */
    List<WmsSupplierVo> queryList(WmsSupplierBo bo);

    /**
     * 新增供应商信息
     */
    Boolean insertByBo(WmsSupplierBo bo);

    /**
     * 修改供应商信息
     */
    Boolean updateByBo(WmsSupplierBo bo);

    /**
     * 校验并批量删除供应商信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}