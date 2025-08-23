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
import org.dromara.hrp.domain.bo.HrpPayrollSettingsBo;
import org.dromara.hrp.domain.vo.HrpPayrollSettingsVo;
import org.dromara.hrp.domain.HrpPayrollSettings;
import org.dromara.hrp.mapper.HrpPayrollSettingsMapper;
import org.dromara.hrp.service.IHrpPayrollSettingsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 薪资规则Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpPayrollSettingsServiceImpl implements IHrpPayrollSettingsService {

    private final HrpPayrollSettingsMapper baseMapper;

    /**
     * 查询薪资规则
     *
     * @param id 主键
     * @return 薪资规则
     */
    @Override
    public HrpPayrollSettingsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询薪资规则列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 薪资规则分页列表
     */
    @Override
    public TableDataInfo<HrpPayrollSettingsVo> queryPageList(HrpPayrollSettingsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpPayrollSettings> lqw = buildQueryWrapper(bo);
        Page<HrpPayrollSettingsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的薪资规则列表
     *
     * @param bo 查询条件
     * @return 薪资规则列表
     */
    @Override
    public List<HrpPayrollSettingsVo> queryList(HrpPayrollSettingsBo bo) {
        LambdaQueryWrapper<HrpPayrollSettings> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpPayrollSettings> buildQueryWrapper(HrpPayrollSettingsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpPayrollSettings> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpPayrollSettings::getId);
        lqw.eq(bo.getUserId() != null, HrpPayrollSettings::getUserId, bo.getUserId());
        lqw.eq(bo.getBaseSalary() != null, HrpPayrollSettings::getBaseSalary, bo.getBaseSalary());
        lqw.eq(bo.getHourlyRate() != null, HrpPayrollSettings::getHourlyRate, bo.getHourlyRate());
        lqw.eq(bo.getEffectiveDate() != null, HrpPayrollSettings::getEffectiveDate, bo.getEffectiveDate());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpPayrollSettings::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增薪资规则
     *
     * @param bo 薪资规则
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpPayrollSettingsBo bo) {
        HrpPayrollSettings add = MapstructUtils.convert(bo, HrpPayrollSettings.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改薪资规则
     *
     * @param bo 薪资规则
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpPayrollSettingsBo bo) {
        HrpPayrollSettings update = MapstructUtils.convert(bo, HrpPayrollSettings.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpPayrollSettings entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除薪资规则信息
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
