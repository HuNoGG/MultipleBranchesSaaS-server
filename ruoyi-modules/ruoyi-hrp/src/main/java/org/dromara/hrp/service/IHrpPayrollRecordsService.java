package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpPayrollRecordsVo;
import org.dromara.hrp.domain.bo.HrpPayrollRecordsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 薪资单记录Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpPayrollRecordsService {

    /**
     * 查询薪资单记录
     *
     * @param id 主键
     * @return 薪资单记录
     */
    HrpPayrollRecordsVo queryById(Long id);

    /**
     * 分页查询薪资单记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 薪资单记录分页列表
     */
    TableDataInfo<HrpPayrollRecordsVo> queryPageList(HrpPayrollRecordsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的薪资单记录列表
     *
     * @param bo 查询条件
     * @return 薪资单记录列表
     */
    List<HrpPayrollRecordsVo> queryList(HrpPayrollRecordsBo bo);

    /**
     * 新增薪资单记录
     *
     * @param bo 薪资单记录
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpPayrollRecordsBo bo);

    /**
     * 修改薪资单记录
     *
     * @param bo 薪资单记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpPayrollRecordsBo bo);

    /**
     * 校验并批量删除薪资单记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
