package org.dromara.hrp.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpPayrollRecordsBo;
import org.dromara.hrp.domain.vo.HrpPayrollRecordsVo;
import org.dromara.hrp.domain.HrpPayrollRecords;
import org.dromara.hrp.mapper.HrpPayrollRecordsMapper;
import org.dromara.hrp.service.IHrpPayrollRecordsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 薪资单记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpPayrollRecordsServiceImpl implements IHrpPayrollRecordsService {

    private final HrpPayrollRecordsMapper baseMapper;

    /**
     * 查询薪资单记录
     *
     * @param id 主键
     * @return 薪资单记录
     */
    @Override
    public HrpPayrollRecordsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询薪资单记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 薪资单记录分页列表
     */
    @Override
    public TableDataInfo<HrpPayrollRecordsVo> queryPageList(HrpPayrollRecordsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpPayrollRecords> lqw = buildQueryWrapper(bo);
        Page<HrpPayrollRecordsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的薪资单记录列表
     *
     * @param bo 查询条件
     * @return 薪资单记录列表
     */
    @Override
    public List<HrpPayrollRecordsVo> queryList(HrpPayrollRecordsBo bo) {
        LambdaQueryWrapper<HrpPayrollRecords> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpPayrollRecords> buildQueryWrapper(HrpPayrollRecordsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpPayrollRecords> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpPayrollRecords::getId);
        lqw.eq(bo.getUserId() != null, HrpPayrollRecords::getUserId, bo.getUserId());
        lqw.eq(bo.getPayPeriodStart() != null, HrpPayrollRecords::getPayPeriodStart, bo.getPayPeriodStart());
        lqw.eq(bo.getPayPeriodEnd() != null, HrpPayrollRecords::getPayPeriodEnd, bo.getPayPeriodEnd());
        lqw.eq(bo.getTotalHours() != null, HrpPayrollRecords::getTotalHours, bo.getTotalHours());
        lqw.eq(bo.getBasePay() != null, HrpPayrollRecords::getBasePay, bo.getBasePay());
        lqw.eq(bo.getOvertimePay() != null, HrpPayrollRecords::getOvertimePay, bo.getOvertimePay());
        lqw.eq(bo.getDeductions() != null, HrpPayrollRecords::getDeductions, bo.getDeductions());
        lqw.eq(bo.getFinalSalary() != null, HrpPayrollRecords::getFinalSalary, bo.getFinalSalary());
        lqw.eq(bo.getIsFinalized() != null, HrpPayrollRecords::getIsFinalized, bo.getIsFinalized());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpPayrollRecords::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增薪资单记录
     *
     * @param bo 薪资单记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpPayrollRecordsBo bo) {
        HrpPayrollRecords add = MapstructUtils.convert(bo, HrpPayrollRecords.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改薪资单记录
     *
     * @param bo 薪资单记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpPayrollRecordsBo bo) {
        HrpPayrollRecords update = MapstructUtils.convert(bo, HrpPayrollRecords.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpPayrollRecords entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除薪资单记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
