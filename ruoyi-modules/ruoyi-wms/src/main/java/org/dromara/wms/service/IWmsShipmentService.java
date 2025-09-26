package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsShipmentVo;
import org.dromara.wms.domain.bo.WmsShipmentBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 出货记录Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsShipmentService {

    /**
     * 查询出货记录
     */
    WmsShipmentVo queryById(Long id);

    /**
     * 查询出货记录列表
     */
    TableDataInfo<WmsShipmentVo> queryPageList(WmsShipmentBo bo, PageQuery pageQuery);

    /**
     * 查询出货记录列表
     */
    List<WmsShipmentVo> queryList(WmsShipmentBo bo);

    /**
     * 新增出货记录
     */
    Boolean insertByBo(WmsShipmentBo bo);

    /**
     * 修改出货记录
     */
    Boolean updateByBo(WmsShipmentBo bo);

    /**
     * 校验并批量删除出货记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}