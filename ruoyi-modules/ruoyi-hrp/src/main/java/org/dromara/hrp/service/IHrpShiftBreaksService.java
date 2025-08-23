package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpShiftBreaksVo;
import org.dromara.hrp.domain.bo.HrpShiftBreaksBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 班别休息时段Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpShiftBreaksService {

    /**
     * 查询班别休息时段
     *
     * @param id 主键
     * @return 班别休息时段
     */
    HrpShiftBreaksVo queryById(Long id);

    /**
     * 分页查询班别休息时段列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班别休息时段分页列表
     */
    TableDataInfo<HrpShiftBreaksVo> queryPageList(HrpShiftBreaksBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的班别休息时段列表
     *
     * @param bo 查询条件
     * @return 班别休息时段列表
     */
    List<HrpShiftBreaksVo> queryList(HrpShiftBreaksBo bo);

    /**
     * 新增班别休息时段
     *
     * @param bo 班别休息时段
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpShiftBreaksBo bo);

    /**
     * 修改班别休息时段
     *
     * @param bo 班别休息时段
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpShiftBreaksBo bo);

    /**
     * 校验并批量删除班别休息时段信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
