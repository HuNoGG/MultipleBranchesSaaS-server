package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpStoreSupportRelationsVo;
import org.dromara.hrp.domain.bo.HrpStoreSupportRelationsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 分店支援关系Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpStoreSupportRelationsService {

    /**
     * 查询分店支援关系
     *
     * @param requestingStoreId 主键
     * @return 分店支援关系
     */
    HrpStoreSupportRelationsVo queryById(Long requestingStoreId);

    /**
     * 分页查询分店支援关系列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分店支援关系分页列表
     */
    TableDataInfo<HrpStoreSupportRelationsVo> queryPageList(HrpStoreSupportRelationsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的分店支援关系列表
     *
     * @param bo 查询条件
     * @return 分店支援关系列表
     */
    List<HrpStoreSupportRelationsVo> queryList(HrpStoreSupportRelationsBo bo);

    /**
     * 新增分店支援关系
     *
     * @param bo 分店支援关系
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpStoreSupportRelationsBo bo);

    /**
     * 修改分店支援关系
     *
     * @param bo 分店支援关系
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpStoreSupportRelationsBo bo);

    /**
     * 校验并批量删除分店支援关系信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
