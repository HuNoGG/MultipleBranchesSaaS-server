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
import org.dromara.hrp.domain.dto.AllRequirementsDto;
import org.dromara.hrp.domain.dto.DailyRequirementsDto;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpScheduleRequirementsBo;
import org.dromara.hrp.domain.vo.HrpScheduleRequirementsVo;
import org.dromara.hrp.domain.HrpScheduleRequirements;
import org.dromara.hrp.mapper.HrpScheduleRequirementsMapper;
import org.dromara.hrp.service.IHrpScheduleRequirementsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 每日人力需求Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpScheduleRequirementsServiceImpl implements IHrpScheduleRequirementsService {

    private final HrpScheduleRequirementsMapper baseMapper;

    /**
     * 查询每日人力需求
     *
     * @param id 主键
     * @return 每日人力需求
     */
    @Override
    public HrpScheduleRequirementsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询每日人力需求列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 每日人力需求分页列表
     */
    @Override
    public TableDataInfo<HrpScheduleRequirementsVo> queryPageList(HrpScheduleRequirementsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpScheduleRequirements> lqw = buildQueryWrapper(bo);
        Page<HrpScheduleRequirementsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的每日人力需求列表
     *
     * @param bo 查询条件
     * @return 每日人力需求列表
     */
    @Override
    public List<HrpScheduleRequirementsVo> queryList(HrpScheduleRequirementsBo bo) {
        LambdaQueryWrapper<HrpScheduleRequirements> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpScheduleRequirements> buildQueryWrapper(HrpScheduleRequirementsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpScheduleRequirements> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpScheduleRequirements::getId);
        lqw.eq(bo.getStoreId() != null, HrpScheduleRequirements::getStoreId, bo.getStoreId());
        lqw.eq(StringUtils.isNotBlank(bo.getDayType()), HrpScheduleRequirements::getDayType, bo.getDayType());
        lqw.eq(bo.getShiftId() != null, HrpScheduleRequirements::getShiftId, bo.getShiftId());
        lqw.eq(bo.getSkillId() != null, HrpScheduleRequirements::getSkillId, bo.getSkillId());
        lqw.eq(bo.getRequiredCount() != null, HrpScheduleRequirements::getRequiredCount, bo.getRequiredCount());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpScheduleRequirements::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增每日人力需求
     *
     * @param bo 每日人力需求
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpScheduleRequirementsBo bo) {
        HrpScheduleRequirements add = MapstructUtils.convert(bo, HrpScheduleRequirements.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改每日人力需求
     *
     * @param bo 每日人力需求
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpScheduleRequirementsBo bo) {
        HrpScheduleRequirements update = MapstructUtils.convert(bo, HrpScheduleRequirements.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpScheduleRequirements entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除每日人力需求信息
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRequirements(DailyRequirementsDto dto) {
        Long storeId = dto.getStoreId();
        String dayType = dto.getDayType();

        // 1. 先删除该店铺、该日期类型下所有旧的人力需求配置
        baseMapper.delete(new LambdaQueryWrapper<HrpScheduleRequirements>()
            .eq(HrpScheduleRequirements::getStoreId, storeId)
            .eq(HrpScheduleRequirements::getDayType, dayType));

        // 2. 准备要插入的新数据
        List<HrpScheduleRequirements> newRequirements = new ArrayList<>();
        if (dto.getRequirements() != null) {
            for (DailyRequirementsDto.RequirementItem item : dto.getRequirements()) {
                // 只保存需求数量大于0的记录
                if (item.getRequiredCount() != null && item.getRequiredCount() > 0) {
                    HrpScheduleRequirements req = new HrpScheduleRequirements();
                    req.setStoreId(storeId);
                    req.setDayType(dayType);
                    req.setShiftId(item.getShiftId());
                    req.setSkillId(item.getSkillId());
                    req.setRequiredCount(item.getRequiredCount().longValue());
                    newRequirements.add(req);
                }
            }
        }

        // 3. 批量插入新的需求配置
        if (!newRequirements.isEmpty()) {
            baseMapper.insertBatch(newRequirements);
        }
    }

    @Override
    public AllRequirementsDto getAllRequirementsByStoreId(Long storeId) {
        // 1. 一次性查询出该店铺下的所有需求记录
        List<HrpScheduleRequirementsVo> allReqs = baseMapper.selectVoList(
            new LambdaQueryWrapper<HrpScheduleRequirements>()
                .eq(HrpScheduleRequirements::getStoreId, storeId)
        );

        // 2. 使用Java Stream API按 dayType 进行分组
        Map<String, List<HrpScheduleRequirementsVo>> groupedByType = allReqs.stream()
            .collect(Collectors.groupingBy(HrpScheduleRequirementsVo::getDayType));

        // 3. 创建DTO并填充数据
        AllRequirementsDto dto = new AllRequirementsDto();
        dto.setWeekday(groupedByType.getOrDefault("weekday", new ArrayList<>()));
        dto.setHoliday(groupedByType.getOrDefault("holiday", new ArrayList<>()));
        dto.setSpecial(groupedByType.getOrDefault("special", new ArrayList<>()));

        return dto;
    }
}
