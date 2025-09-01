package org.dromara.hrp.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hrp.domain.HrpScheduleModifications;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.dto.SchedulePlanDto;
import org.dromara.hrp.domain.dto.WeeklyScheduleDto;
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.mapper.HrpScheduleModificationsMapper;
import org.dromara.hrp.mapper.HrpUserProfileMapper;
import org.dromara.hrp.service.IHrpSkillsService;
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
    private HrpScheduleModificationsMapper modificationsMapper;

    @Autowired
    private IHrpSkillsService hrpSkillsService;

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
                new LambdaQueryWrapper<HrpUserProfile>()
                    .in(HrpUserProfile::getUserId, userIds)
            );
            if(!employees.isEmpty()){
                for (HrpUserProfileVo userProfile : employees) {
                    List<HrpSkillsVo> skillList = hrpSkillsService.queryListWithUserSkillByUserId(userProfile.getUserId(),storeId);
                    userProfile.setSkills(skillList);
                }
            }

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
    public WeeklyScheduleDto getWeeklySchedule(Long storeId, String startDate, String endDate) {
        WeeklyScheduleDto weeklyDto = new WeeklyScheduleDto();

        // 1. 查询该时间范围内的所有排班记录
        List<HrpSchedulesVo> schedules = baseMapper.selectSchedulePlanList(storeId, startDate, endDate);

        // 2. 判断排班状态
        String scheduleStatus = "DRAFT"; // 默认为草稿
        if (!schedules.isEmpty() && "PUBLISHED".equals(schedules.get(0).getStatus())) {
            scheduleStatus = "PUBLISHED";
        }
        weeklyDto.setStatus(scheduleStatus);

        // 如果是已发布状态，则需要关联考勤信息
        if ("PUBLISHED".equals(scheduleStatus)) {
            schedules = baseMapper.selectScheduleHistoryList(storeId, startDate, endDate);
        } else {
            schedules = baseMapper.selectSchedulePlanList(storeId, startDate, endDate);
        }

        if ("PUBLISHED".equals(scheduleStatus)) {
            // a. 查询所有类型为“新增临时”的修改记录
            LambdaQueryWrapper<HrpScheduleModifications> lqw = Wrappers.lambdaQuery();
            lqw.eq(HrpScheduleModifications::getChangeType, "新增临时");
            List<HrpScheduleModifications> externalMods = modificationsMapper.selectList(lqw);

            if (CollUtil.isNotEmpty(externalMods)) {
                // b. 获取这些修改记录对应的排班ID
                List<Long> scheduleIds = externalMods.stream()
                    .map(HrpScheduleModifications::getScheduleId)
                    .collect(Collectors.toList());
                // c. 查询这些排班的详细信息（包含班次、技能等）
                List<HrpSchedulesVo> externalSchedules = baseMapper.selectScheduleHistoryBatch(scheduleIds);

                // d. 将remark信息合并到排班Vo中
                Map<Long, HrpScheduleModifications> modsMap = externalMods.stream()
                    .collect(Collectors.toMap(HrpScheduleModifications::getScheduleId, mod -> mod));

                for (HrpSchedulesVo vo : externalSchedules) {
                    HrpScheduleModifications mod = modsMap.get(vo.getId());
                    if (mod != null) {
                        JSONObject remarkJson = JSONUtil.parseObj(mod.getRemark());
                        // 使用remark中的信息覆盖vo的默认值
                        vo.setUserName(remarkJson.getStr("employeeName")); // 外部人员姓名
                        vo.setAttendanceStatus("增补"); // 直接设置状态为增补
                        if (remarkJson.getStr("tag") != null) {
                            vo.setShiftName(vo.getShiftName() + " (" + remarkJson.getStr("tag") + ")");
                        }
                    }
                }
                // e. 将处理过的外部人员排班添加到总列表中
                schedules.addAll(externalSchedules);
            }
        }

        Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows = schedules.stream()
            .collect(Collectors.groupingBy(
                HrpSchedulesVo::getUserId,
                Collectors.groupingBy(vo -> DateUtil.format(vo.getScheduleDate(), "yyyy-MM-dd"))
            ));

        // 3. 组织数据
        Set<Long> userIds = schedules.stream().map(HrpSchedulesVo::getUserId).collect(Collectors.toSet());
        List<HrpUserProfileVo> employees;
        if (!userIds.isEmpty()) {
            employees = userProfileMapper.selectVoList(new LambdaQueryWrapper<HrpUserProfile>().in(HrpUserProfile::getUserId, userIds));
            if(!employees.isEmpty()){
                for (HrpUserProfileVo userProfile : employees) {
                    List<HrpSkillsVo> skillList = hrpSkillsService.queryListWithUserSkillByUserId(userProfile.getUserId(),storeId);
                    userProfile.setSkills(skillList);
                }
            }
        }
        else
        {
            employees = new ArrayList<>();
        }
        schedules.stream().filter(schedulesVo -> schedulesVo.getUserId() == 0).forEach(item -> {
            JSONObject remarkJson = JSONUtil.parseObj(item.getRemark());
            if(ObjectUtil.isAllEmpty(remarkJson)){
                return;
            }
            String externalEmployeeName = remarkJson.getStr("employeeName");
            String position = remarkJson.getStr("position");
            HrpUserProfileVo externalEmployee = new HrpUserProfileVo();
            externalEmployee.setUserId(0L);
            externalEmployee.setMainStoreId(storeId);
            externalEmployee.setPriorityScore(0L);
            externalEmployee.setUserName(externalEmployeeName);
            HrpSkillsVo skill = hrpSkillsService.queryById(Long.parseLong(position));
            externalEmployee.setSkills(Collections.singletonList(skill));
            employees.add(externalEmployee);
        });


        weeklyDto.setDates(buildDateHeaders(startDate, endDate));
        weeklyDto.setEmployees(employees);
        weeklyDto.setScheduleRows(scheduleRows);

        return weeklyDto;
    }

    @Override
    public Boolean publishSchedule(Long storeId, String startDate, String endDate) {
        boolean b = baseMapper.publishSchedules(storeId, LocalDate.parse(startDate), LocalDate.parse(endDate)) > 0;
        return b;
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
            if(!employees.isEmpty()){
                for (HrpUserProfileVo userProfile : employees) {
                    List<HrpSkillsVo> skillList = hrpSkillsService.queryListWithUserSkillByUserId(userProfile.getUserId(),storeId);
                    userProfile.setSkills(skillList);
                }
            }
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
    public ScheduleGenerationResult generateSchedule(ScheduleGenerateDto dto) {
        // 调用核心算法服务来生成排班
        return schedulingAlgorithmService.generateSchedule(dto);
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
