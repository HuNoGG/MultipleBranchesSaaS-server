package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpInventoryTransfersVo;
import org.dromara.hrp.domain.bo.HrpInventoryTransfersBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 调货记录Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpInventoryTransfersService {

    /**
     * 查询调货记录
     *
     * @param id 主键
     * @return 调货记录
     */
    HrpInventoryTransfersVo queryById(Long id);

    /**
     * 分页查询调货记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 调货记录分页列表
     */
    TableDataInfo<HrpInventoryTransfersVo> queryPageList(HrpInventoryTransfersBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的调货记录列表
     *
     * @param bo 查询条件
     * @return 调货记录列表
     */
    List<HrpInventoryTransfersVo> queryList(HrpInventoryTransfersBo bo);

    /**
     * 新增调货记录
     *
     * @param bo 调货记录
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpInventoryTransfersBo bo);

    /**
     * 修改调货记录
     *
     * @param bo 调货记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpInventoryTransfersBo bo);

    /**
     * 校验并批量删除调货记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
