package org.dromara.hrp.service.impl;

import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hrp.domain.HrpScheduleRequirements;
import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.hrp.domain.HrpStores;
import org.dromara.hrp.domain.bo.HrpShiftBreaksBo;
import org.dromara.hrp.domain.dto.BasicSettingsDto;
import org.dromara.hrp.domain.vo.HrpShiftBreaksVo;
import org.dromara.hrp.mapper.HrpScheduleRequirementsMapper;
import org.dromara.hrp.mapper.HrpShiftBreaksMapper;
import org.dromara.hrp.mapper.HrpStoresMapper;
import org.dromara.hrp.service.IHrpShiftBreaksService;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpShiftsBo;
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.domain.HrpShifts;
import org.dromara.hrp.mapper.HrpShiftsMapper;
import org.dromara.hrp.service.IHrpShiftsService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 班别设定Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpShiftsServiceImpl implements IHrpShiftsService {

    private final HrpShiftsMapper baseMapper;
    private final IHrpShiftBreaksService hrpShiftBreaksService;
    private final HrpShiftBreaksMapper shiftBreaksMapper;
    private final HrpStoresMapper storesMapper;
    private final HrpScheduleRequirementsMapper scheduleRequirementsMapper;

    /**
     * 查询班别设定
     *
     * @param id 主键
     * @return 班别设定
     */
    @Override
    public HrpShiftsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询班别设定列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班别设定分页列表
     */
    @Override
    public TableDataInfo<HrpShiftsVo> queryPageList(HrpShiftsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpShifts> lqw = buildQueryWrapper(bo);
        Page<HrpShiftsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的班别设定列表
     *
     * @param bo 查询条件
     * @return 班别设定列表
     */
    @Override
    public List<HrpShiftsVo> queryList(HrpShiftsBo bo) {
        LambdaQueryWrapper<HrpShifts> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpShifts> buildQueryWrapper(HrpShiftsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpShifts> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpShifts::getId);
        lqw.eq(bo.getStoreId() != null, HrpShifts::getStoreId, bo.getStoreId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HrpShifts::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), HrpShifts::getCode, bo.getCode());
        lqw.eq(bo.getStartTime() != null, HrpShifts::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, HrpShifts::getEndTime, bo.getEndTime());
        lqw.eq(bo.getIsCrossDay() != null, HrpShifts::getIsCrossDay, bo.getIsCrossDay());
        lqw.eq(StringUtils.isNotBlank(bo.getColorCode()), HrpShifts::getColorCode, bo.getColorCode());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpShifts::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增班别设定
     *
     * @param bo 班别设定
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpShiftsBo bo) {
        HrpShifts add = MapstructUtils.convert(bo, HrpShifts.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改班别设定
     *
     * @param bo 班别设定
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpShiftsBo bo) {
        HrpShifts update = MapstructUtils.convert(bo, HrpShifts.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpShifts entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除班别设定信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        if(isValid){
            //TODO 未来可在此处做一些业务校验，例如检查是否有关联的“已发布”排班记录
        }
        // 1. 删除与这些班次相关联的所有人力需求
        scheduleRequirementsMapper.delete(new LambdaQueryWrapper<HrpScheduleRequirements>()
            .in(HrpScheduleRequirements::getShiftId, ids));

        // 2. 删除与这些班次相关联的所有休息时间
        shiftBreaksMapper.delete(new LambdaQueryWrapper<HrpShiftBreaks>()
            .in(HrpShiftBreaks::getShiftId, ids));

        // 3. 最后删除班次本身
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 查询班次和休息时间
     * @param bo 班次查询条件
     * @return 班次和休息时间
     */
    @Override
    public List<HrpShiftsVo> queryShiftsAndRestTime(HrpShiftsBo bo) {
        List<HrpShiftsVo> result = new ArrayList<>();
        // 1. 查询当前店铺的班次集合
        List<HrpShiftsVo> hrpShiftsVos = queryList(bo);
        hrpShiftsVos.forEach(hrpShiftsVo -> {
            // 2. 查询当前班次的休息时间
            HrpShiftBreaksBo hrpShiftBreaksBo = new HrpShiftBreaksBo();
            hrpShiftBreaksBo.setShiftId(hrpShiftsVo.getId());
            List<HrpShiftBreaksVo> hrpShiftBreaksVos = hrpShiftBreaksService.queryList(hrpShiftBreaksBo);
            hrpShiftsVo.setShiftBreaksList(hrpShiftBreaksVos);
            result.add(hrpShiftsVo);
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveShiftsAndSettings(BasicSettingsDto dto) {
        // 1. 保存其他基础设定 (如跨日规则)
        HrpStores store = storesMapper.selectById(dto.getStoreId());
        if (store != null) {
            store.setCrossDayRule(dto.getCrossDayRule());
            storesMapper.updateById(store);
        }

        // 2. 处理班次和休息时间的增删改
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (BasicSettingsDto.ShiftDto shiftDto : dto.getShifts()) {
            HrpShifts shift = new HrpShifts();
            shift.setName(shiftDto.getName());
            shift.setCode(shiftDto.getCode());
            shift.setColorCode(shiftDto.getColorCode());
            shift.setIsCrossDay(shiftDto.isCrossDay());
            shift.setStoreId(dto.getStoreId());
            try {
                shift.setStartTime(LocalTime.parse(shiftDto.getStartTime(), timeFormatter));
                shift.setEndTime(LocalTime.parse(shiftDto.getEndTime(), timeFormatter));
            } catch (Exception e) {
                throw new ServiceException("时间格式错误");
            }

            // 判断是新增还是更新
            if (shiftDto.getId() == null || shiftDto.getId() == 0) {
                // 新增班次
                baseMapper.insert(shift); // 插入后，shift对象会自动填充ID
            } else {
                // 更新班次
                shift.setId(shiftDto.getId());
                baseMapper.updateById(shift);
            }

            // 3. 处理该班次下的休息时间 (采用先删后增的策略，最简单高效)
            // 先删除该班次所有旧的休息时间
            shiftBreaksMapper.delete(new LambdaQueryWrapper<HrpShiftBreaks>()
                .eq(HrpShiftBreaks::getShiftId, shift.getId()));

            // 再插入所有新的休息时间
            if (shiftDto.getBreakTimes() != null) {
                for (BasicSettingsDto.BreakDto breakDto : shiftDto.getBreakTimes()) {
                    if (breakDto.getRange() != null && breakDto.getRange().length == 2) {
                        HrpShiftBreaks shiftBreak = new HrpShiftBreaks();
                        shiftBreak.setShiftId(shift.getId());
                        try {
                            shiftBreak.setBreakStartTime(LocalTime.parse(breakDto.getRange()[0], timeFormatter));
                            shiftBreak.setBreakEndTime(LocalTime.parse(breakDto.getRange()[1], timeFormatter));
                        } catch (Exception e) {
                            throw new ServiceException("休息时间格式错误");
                        }
                        shiftBreaksMapper.insert(shiftBreak);
                    }
                }
            }
        }
    }
}
