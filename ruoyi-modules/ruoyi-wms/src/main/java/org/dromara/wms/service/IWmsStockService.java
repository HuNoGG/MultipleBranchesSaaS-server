package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsStockVo;
import org.dromara.wms.domain.bo.WmsStockBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 库存Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsStockService {

    /**
     * 查询库存
     */
    WmsStockVo queryById(Long id);

    /**
     * 查询库存列表
     */
    TableDataInfo<WmsStockVo> queryPageList(WmsStockBo bo, PageQuery pageQuery);

    /**
     * 查询库存列表
     */
    List<WmsStockVo> queryList(WmsStockBo bo);

    /**
     * 新增库存
     */
    Boolean insertByBo(WmsStockBo bo);

    /**
     * 修改库存
     */
    Boolean updateByBo(WmsStockBo bo);

    /**
     * 校验并批量删除库存信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}