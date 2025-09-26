package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsProductVo;
import org.dromara.wms.domain.bo.WmsProductBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品信息Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsProductService {

    /**
     * 查询商品信息
     */
    WmsProductVo queryById(Long id);

    /**
     * 查询商品信息列表
     */
    TableDataInfo<WmsProductVo> queryPageList(WmsProductBo bo, PageQuery pageQuery);

    /**
     * 查询商品信息列表
     */
    List<WmsProductVo> queryList(WmsProductBo bo);

    /**
     * 新增商品信息
     */
    Boolean insertByBo(WmsProductBo bo);

    /**
     * 修改商品信息
     */
    Boolean updateByBo(WmsProductBo bo);

    /**
     * 校验并批量删除商品信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}