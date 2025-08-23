package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpLeaveApplicationsVo;
import org.dromara.hrp.domain.bo.HrpLeaveApplicationsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 正式请假申请Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpLeaveApplicationsService {

    /**
     * 查询正式请假申请
     *
     * @param id 主键
     * @return 正式请假申请
     */
    HrpLeaveApplicationsVo queryById(Long id);

    /**
     * 分页查询正式请假申请列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 正式请假申请分页列表
     */
    TableDataInfo<HrpLeaveApplicationsVo> queryPageList(HrpLeaveApplicationsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的正式请假申请列表
     *
     * @param bo 查询条件
     * @return 正式请假申请列表
     */
    List<HrpLeaveApplicationsVo> queryList(HrpLeaveApplicationsBo bo);

    /**
     * 新增正式请假申请
     *
     * @param bo 正式请假申请
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpLeaveApplicationsBo bo);

    /**
     * 修改正式请假申请
     *
     * @param bo 正式请假申请
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpLeaveApplicationsBo bo);

    /**
     * 校验并批量删除正式请假申请信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
