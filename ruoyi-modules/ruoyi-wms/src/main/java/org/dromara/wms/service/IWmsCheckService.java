package org.dromara.wms.service;

import org.dromara.wms.domain.vo.WmsCheckVo;
import org.dromara.wms.domain.bo.WmsCheckBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 点货Service接口
 *
 * @author Jules
 * @date 2025-09-26
 */
public interface IWmsCheckService {

    /**
     * 查询点货
     */
    WmsCheckVo queryById(Long id);

    /**
     * 查询点货列表
     */
    TableDataInfo<WmsCheckVo> queryPageList(WmsCheckBo bo, PageQuery pageQuery);

    /**
     * 查询点货列表
     */
    List<WmsCheckVo> queryList(WmsCheckBo bo);

    /**
     * 新增点货
     */
    Boolean insertByBo(WmsCheckBo bo);

    /**
     * 修改点货
     */
    Boolean updateByBo(WmsCheckBo bo);

    /**
     * 校验并批量删除点货信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}