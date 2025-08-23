package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpAttendanceExceptionsVo;
import org.dromara.hrp.domain.bo.HrpAttendanceExceptionsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 考勤异常Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpAttendanceExceptionsService {

    /**
     * 查询考勤异常
     *
     * @param id 主键
     * @return 考勤异常
     */
    HrpAttendanceExceptionsVo queryById(Long id);

    /**
     * 分页查询考勤异常列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 考勤异常分页列表
     */
    TableDataInfo<HrpAttendanceExceptionsVo> queryPageList(HrpAttendanceExceptionsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的考勤异常列表
     *
     * @param bo 查询条件
     * @return 考勤异常列表
     */
    List<HrpAttendanceExceptionsVo> queryList(HrpAttendanceExceptionsBo bo);

    /**
     * 新增考勤异常
     *
     * @param bo 考勤异常
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpAttendanceExceptionsBo bo);

    /**
     * 修改考勤异常
     *
     * @param bo 考勤异常
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpAttendanceExceptionsBo bo);

    /**
     * 校验并批量删除考勤异常信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
