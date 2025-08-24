package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpScheduleModificationsVo;
import org.dromara.hrp.domain.bo.HrpScheduleModificationsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 排班修改记录Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpScheduleModificationsService {

    /**
     * 查询排班修改记录
     *
     * @param id 主键
     * @return 排班修改记录
     */
    HrpScheduleModificationsVo queryById(Long id);

    /**
     * 分页查询排班修改记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排班修改记录分页列表
     */
    TableDataInfo<HrpScheduleModificationsVo> queryPageList(HrpScheduleModificationsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的排班修改记录列表
     *
     * @param bo 查询条件
     * @return 排班修改记录列表
     */
    List<HrpScheduleModificationsVo> queryList(HrpScheduleModificationsBo bo);

    /**
     * 新增排班修改记录
     *
     * @param bo 排班修改记录
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpScheduleModificationsBo bo);

    /**
     * 批量新增排班修改记录
     *
     * @param boList 待新增的业务对象列表
     * @return 是否新增成功
     */
    Boolean insertBatchByBo(List<HrpScheduleModificationsBo> boList);

    /**
     * 修改排班修改记录
     *
     * @param bo 排班修改记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpScheduleModificationsBo bo);

    /**
     * 校验并批量删除排班修改记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
