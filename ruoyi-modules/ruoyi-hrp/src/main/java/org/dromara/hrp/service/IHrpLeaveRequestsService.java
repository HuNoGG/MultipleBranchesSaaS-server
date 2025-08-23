package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpLeaveRequestsVo;
import org.dromara.hrp.domain.bo.HrpLeaveRequestsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 期望休假/排休记录Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpLeaveRequestsService {

    /**
     * 查询期望休假/排休记录
     *
     * @param id 主键
     * @return 期望休假/排休记录
     */
    HrpLeaveRequestsVo queryById(Long id);

    /**
     * 分页查询期望休假/排休记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 期望休假/排休记录分页列表
     */
    TableDataInfo<HrpLeaveRequestsVo> queryPageList(HrpLeaveRequestsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的期望休假/排休记录列表
     *
     * @param bo 查询条件
     * @return 期望休假/排休记录列表
     */
    List<HrpLeaveRequestsVo> queryList(HrpLeaveRequestsBo bo);

    /**
     * 新增期望休假/排休记录
     *
     * @param bo 期望休假/排休记录
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpLeaveRequestsBo bo);

    /**
     * 修改期望休假/排休记录
     *
     * @param bo 期望休假/排休记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpLeaveRequestsBo bo);

    /**
     * 校验并批量删除期望休假/排休记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
