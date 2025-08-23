package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.domain.bo.HrpUserAvailabilityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工可上班时段Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpUserAvailabilityService {

    /**
     * 查询员工可上班时段
     *
     * @param id 主键
     * @return 员工可上班时段
     */
    HrpUserAvailabilityVo queryById(Long id);

    /**
     * 分页查询员工可上班时段列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工可上班时段分页列表
     */
    TableDataInfo<HrpUserAvailabilityVo> queryPageList(HrpUserAvailabilityBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工可上班时段列表
     *
     * @param bo 查询条件
     * @return 员工可上班时段列表
     */
    List<HrpUserAvailabilityVo> queryList(HrpUserAvailabilityBo bo);

    /**
     * 新增员工可上班时段
     *
     * @param bo 员工可上班时段
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpUserAvailabilityBo bo);

    /**
     * 修改员工可上班时段
     *
     * @param bo 员工可上班时段
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpUserAvailabilityBo bo);

    /**
     * 校验并批量删除员工可上班时段信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
