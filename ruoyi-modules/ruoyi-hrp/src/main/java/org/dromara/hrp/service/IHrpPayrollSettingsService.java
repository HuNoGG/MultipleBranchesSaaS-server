package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpPayrollSettingsVo;
import org.dromara.hrp.domain.bo.HrpPayrollSettingsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 薪资规则Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpPayrollSettingsService {

    /**
     * 查询薪资规则
     *
     * @param id 主键
     * @return 薪资规则
     */
    HrpPayrollSettingsVo queryById(Long id);

    /**
     * 分页查询薪资规则列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 薪资规则分页列表
     */
    TableDataInfo<HrpPayrollSettingsVo> queryPageList(HrpPayrollSettingsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的薪资规则列表
     *
     * @param bo 查询条件
     * @return 薪资规则列表
     */
    List<HrpPayrollSettingsVo> queryList(HrpPayrollSettingsBo bo);

    /**
     * 新增薪资规则
     *
     * @param bo 薪资规则
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpPayrollSettingsBo bo);

    /**
     * 修改薪资规则
     *
     * @param bo 薪资规则
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpPayrollSettingsBo bo);

    /**
     * 校验并批量删除薪资规则信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
