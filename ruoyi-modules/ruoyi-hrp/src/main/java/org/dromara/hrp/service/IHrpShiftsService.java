package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.domain.bo.HrpShiftsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 班别设定Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpShiftsService {

    /**
     * 查询班别设定
     *
     * @param id 主键
     * @return 班别设定
     */
    HrpShiftsVo queryById(Long id);

    /**
     * 分页查询班别设定列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班别设定分页列表
     */
    TableDataInfo<HrpShiftsVo> queryPageList(HrpShiftsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的班别设定列表
     *
     * @param bo 查询条件
     * @return 班别设定列表
     */
    List<HrpShiftsVo> queryList(HrpShiftsBo bo);

    /**
     * 新增班别设定
     *
     * @param bo 班别设定
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpShiftsBo bo);

    /**
     * 修改班别设定
     *
     * @param bo 班别设定
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpShiftsBo bo);

    /**
     * 校验并批量删除班别设定信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
