package org.dromara.hrp.service.impl;

import cn.hutool.core.date.DateUtil;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hrp.domain.dto.SchedulePlanDto;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.mapper.HrpUserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpSchedulesBo;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.mapper.HrpSchedulesMapper;
import org.dromara.hrp.service.IHrpSchedulesService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排班Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpSchedulesServiceImpl implements IHrpSchedulesService {

    private final HrpSchedulesMapper baseMapper;


    @Autowired
    private HrpUserProfileMapper userProfileMapper;
    @Autowired
    private SchedulingAlgorithmService schedulingAlgorithmService; // 注入算法服务





    private static final String[] WEEK_DAYS = {"一", "二", "三", "四", "五", "六", "日"};

    @Override
    public SchedulePlanDto getSchedulePlan(Long storeId, String startDate, String endDate) {
        // 1. 查询时间范围内的所有排班记录 (包含关联表信息)
        List<HrpSchedulesVo> schedules = baseMapper.selectSchedulePlanList(storeId, startDate, endDate);

        // 2. 获取所有涉及的员工ID
        Set<Long> userIds = schedules.stream()
            .map(HrpSchedulesVo::getUserId)
            .collect(Collectors.toSet());

        List<HrpUserProfileVo> employees = new ArrayList<>();
        if (!userIds.isEmpty()) {
            employees = userProfileMapper.selectVoList(
                new LambdaQueryWrapper<org.dromara.hrp.domain.HrpUserProfile>()
                    .in(org.dromara.hrp.domain.HrpUserProfile::getUserId, userIds)
            );
        }

        // 3. 按员工ID和日期对排班数据进行分组
        Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows = schedules.stream()
            .collect(Collectors.groupingBy(
                HrpSchedulesVo::getUserId,
                Collectors.groupingBy(
                    vo -> DateUtil.format(vo.getScheduleDate(), "yyyy-MM-dd")
                )
            ));

        // 4. 构建DTO并返回
        SchedulePlanDto dto = new SchedulePlanDto();
        dto.setDates(buildDateHeaders(startDate, endDate));
        dto.setEmployees(employees);
        dto.setScheduleRows(scheduleRows);

        return dto;
    }

    @Override
    public SchedulePlanDto getScheduleHistory(Long storeId, String startDate, String endDate) {
        // 1. 查询时间范围内的所有排班历史记录 (包含考勤状态)
        List<HrpSchedulesVo> schedules = baseMapper.selectScheduleHistoryList(storeId, startDate, endDate);

        // 2. 后续逻辑与getSchedulePlan完全相同，可以复用
        Set<Long> userIds = schedules.stream()
            .map(HrpSchedulesVo::getUserId)
            .collect(Collectors.toSet());

        List<HrpUserProfileVo> employees = new ArrayList<>();
        if (!userIds.isEmpty()) {
            employees = userProfileMapper.selectVoList(
                new LambdaQueryWrapper<org.dromara.hrp.domain.HrpUserProfile>()
                    .in(org.dromara.hrp.domain.HrpUserProfile::getUserId, userIds)
            );
        }

        Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows = schedules.stream()
            .collect(Collectors.groupingBy(
                HrpSchedulesVo::getUserId,
                Collectors.groupingBy(
                    vo -> DateUtil.format(vo.getScheduleDate(), "yyyy-MM-dd")
                )
            ));

        SchedulePlanDto dto = new SchedulePlanDto();
        dto.setDates(buildDateHeaders(startDate, endDate));
        dto.setEmployees(employees);
        dto.setScheduleRows(scheduleRows);

        return dto;
    }


    @Override
    public void generateSchedule(HrpSchedulesBo bo) {
        // 调用核心算法服务来生成排班
        schedulingAlgorithmService.generateSchedule(bo);
    }


    /**
     * 构建日期表头
     */
    private List<Map<String, String>> buildDateHeaders(String startDate, String endDate) {
        List<Map<String, String>> dateHeaders = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Map<String, String> dateMap = new HashMap<>();
            dateMap.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            dateMap.put("label", "周" + dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.CHINESE));
            dateHeaders.add(dateMap);
        }
        return dateHeaders;
    }


    /**
     * 查询排班
     *
     * @param id 主键
     * @return 排班
     */
    @Override
    public HrpSchedulesVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询排班列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排班分页列表
     */
    @Override
    public TableDataInfo<HrpSchedulesVo> queryPageList(HrpSchedulesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpSchedules> lqw = buildQueryWrapper(bo);
        Page<HrpSchedulesVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的排班列表
     *
     * @param bo 查询条件
     * @return 排班列表
     */
    @Override
    public List<HrpSchedulesVo> queryList(HrpSchedulesBo bo) {
        LambdaQueryWrapper<HrpSchedules> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpSchedules> buildQueryWrapper(HrpSchedulesBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpSchedules> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpSchedules::getId);
        lqw.eq(bo.getUserId() != null, HrpSchedules::getUserId, bo.getUserId());
        lqw.eq(bo.getStoreId() != null, HrpSchedules::getStoreId, bo.getStoreId());
        lqw.eq(bo.getShiftId() != null, HrpSchedules::getShiftId, bo.getShiftId());
        lqw.eq(bo.getSkillId() != null, HrpSchedules::getSkillId, bo.getSkillId());
        lqw.eq(bo.getScheduleDate() != null, HrpSchedules::getScheduleDate, bo.getScheduleDate());
        return lqw;
    }

    /**
     * 新增排班
     *
     * @param bo 排班
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpSchedulesBo bo) {
        HrpSchedules add = MapstructUtils.convert(bo, HrpSchedules.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改排班
     *
     * @param bo 排班
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpSchedulesBo bo) {
        HrpSchedules update = MapstructUtils.convert(bo, HrpSchedules.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpSchedules entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除排班信息
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
