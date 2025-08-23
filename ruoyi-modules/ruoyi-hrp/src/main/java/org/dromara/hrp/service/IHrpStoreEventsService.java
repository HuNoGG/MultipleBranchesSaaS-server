package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpStoreEventsVo;
import org.dromara.hrp.domain.bo.HrpStoreEventsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 分店特殊事件Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpStoreEventsService {

    /**
     * 查询分店特殊事件
     *
     * @param id 主键
     * @return 分店特殊事件
     */
    HrpStoreEventsVo queryById(Long id);

    /**
     * 分页查询分店特殊事件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分店特殊事件分页列表
     */
    TableDataInfo<HrpStoreEventsVo> queryPageList(HrpStoreEventsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的分店特殊事件列表
     *
     * @param bo 查询条件
     * @return 分店特殊事件列表
     */
    List<HrpStoreEventsVo> queryList(HrpStoreEventsBo bo);

    /**
     * 新增分店特殊事件
     *
     * @param bo 分店特殊事件
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpStoreEventsBo bo);

    /**
     * 修改分店特殊事件
     *
     * @param bo 分店特殊事件
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpStoreEventsBo bo);

    /**
     * 校验并批量删除分店特殊事件信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
