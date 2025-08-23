package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpStoresVo;
import org.dromara.hrp.domain.bo.HrpStoresBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 分店Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpStoresService {

    /**
     * 查询分店
     *
     * @param id 主键
     * @return 分店
     */
    HrpStoresVo queryById(Long id);

    /**
     * 分页查询分店列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分店分页列表
     */
    TableDataInfo<HrpStoresVo> queryPageList(HrpStoresBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的分店列表
     *
     * @param bo 查询条件
     * @return 分店列表
     */
    List<HrpStoresVo> queryList(HrpStoresBo bo);

    /**
     * 新增分店
     *
     * @param bo 分店
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpStoresBo bo);

    /**
     * 修改分店
     *
     * @param bo 分店
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpStoresBo bo);

    /**
     * 校验并批量删除分店信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
