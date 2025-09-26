package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsPurchaseVo;
import org.dromara.wms.domain.bo.WmsPurchaseBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 进货记录Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsPurchaseService {

    /**
     * 查询进货记录
     */
    WmsPurchaseVo queryById(Long id);

    /**
     * 查询进货记录列表
     */
    TableDataInfo<WmsPurchaseVo> queryPageList(WmsPurchaseBo bo, PageQuery pageQuery);

    /**
     * 查询进货记录列表
     */
    List<WmsPurchaseVo> queryList(WmsPurchaseBo bo);

    /**
     * 新增进货记录
     */
    Boolean insertByBo(WmsPurchaseBo bo);

    /**
     * 修改进货记录
     */
    Boolean updateByBo(WmsPurchaseBo bo);

    /**
     * 校验并批量删除进货记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}