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
    public WeeklyScheduleDto getWeeklySchedule(Long storeId, String startDate, String endDate) {
        WeeklyScheduleDto weeklyDto = new WeeklyScheduleDto();

        // 1. 【问题修复】: 分别调用新的Mapper方法查询完整班次和替班时段
        List<HrpSchedulesVo> regularSchedules = baseMapper.selectRegularScheduleList(storeId, startDate, endDate);
        List<HrpSchedulesVo> breakCoverageSchedules = baseMapper.selectBreakCoverageScheduleList(storeId, startDate, endDate);

        // 2. 合并两种排班记录
        List<HrpSchedulesVo> allSchedules = new ArrayList<>();
        allSchedules.addAll(regularSchedules);
        allSchedules.addAll(breakCoverageSchedules);

        // 3. 判断整体排班状态
        String scheduleStatus = "DRAFT";
        if (allSchedules.stream().anyMatch(s -> "PUBLISHED".equals(s.getStatus()))) {
            scheduleStatus = "PUBLISHED";
        }
        weeklyDto.setStatus(scheduleStatus);

        // 4. 如果是已发布状态，需要重新获取包含考勤信息的数据
        if ("PUBLISHED".equals(scheduleStatus)) {
            // 注意：selectScheduleHistoryList 应该也包含完整班次和替班时段，这里假设它能查出所有记录
            allSchedules = new ArrayList<>(baseMapper.selectScheduleHistoryList(storeId, startDate, endDate));
        }

        // 5. 格式化替班记录，为其赋予特殊的显示名称和颜色
        for (HrpSchedulesVo vo : allSchedules) {
            // 如果 shiftId 为 null, 则判定为替班记录
            if (vo.getShiftId() == null) {
                if (vo.getSkillName() != null) {
                    vo.setShiftName(vo.getSkillName() + " (替)"); // 例如: "收银 (替)"
                } else {
                    vo.setShiftName("替班");
                }
                // 【问题修复】: 在此处为替班记录设置一个独特的、醒目的颜色
                vo.setColorCode("#B39DDB"); // 例如，一个淡紫色
            }
        }

        // 6. 【保留逻辑】如果状态为“已发布”，处理“新增临时”的外部人员记录
        if ("PUBLISHED".equals(scheduleStatus)) {
            LambdaQueryWrapper<HrpScheduleModifications> lqw = Wrappers.lambdaQuery();
            lqw.eq(HrpScheduleModifications::getChangeType, "新增临时");
            List<HrpScheduleModifications> externalMods = modificationsMapper.selectList(lqw);

            if (CollUtil.isNotEmpty(externalMods)) {
                List<Long> scheduleIds = externalMods.stream()
                    .map(HrpScheduleModifications::getScheduleId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

                if (!scheduleIds.isEmpty()) {
                    List<HrpSchedulesVo> externalSchedules = baseMapper.selectScheduleHistoryBatch(scheduleIds);
                    Map<Long, HrpScheduleModifications> modsMap = externalMods.stream()
                        .collect(Collectors.toMap(HrpScheduleModifications::getScheduleId, mod -> mod, (o1, o2) -> o1));

                    for (HrpSchedulesVo vo : externalSchedules) {
                        HrpScheduleModifications mod = modsMap.get(vo.getId());
                        if (mod != null && mod.getRemark() != null) {
                            try {
                                JSONObject remarkJson = JSONUtil.parseObj(mod.getRemark());
                                vo.setUserName(remarkJson.getStr("employeeName"));
                                vo.setAttendanceStatus("增补");
                                if (remarkJson.getStr("tag") != null) {
                                    vo.setShiftName(vo.getShiftName() + " (" + remarkJson.getStr("tag") + ")");
                                }
                            } catch (Exception e) {
                                log.error("解析临时新增人员的备注信息时出错, 修改记录ID: {}, 备注: {}", mod.getId(), mod.getRemark(), e);
                            }
                        }
                    }
                    allSchedules.addAll(externalSchedules);
                }
            }
        }

        // 7. 按员工ID和日期对所有处理过的排班数据进行分组
        Map<Long, Map<String, List<HrpSchedulesVo>>> scheduleRows = allSchedules.stream()
            .collect(Collectors.groupingBy(
                HrpSchedulesVo::getUserId,
                Collectors.groupingBy(vo -> DateUtil.format(vo.getScheduleDate(), "yyyy-MM-dd"))
            ));

        // 8. 组织员工信息
        Set<Long> userIds = allSchedules.stream().map(HrpSchedulesVo::getUserId).collect(Collectors.toSet());
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
        else {
            employees = new ArrayList<>();
        }

        // 9. 【保留逻辑】处理 userId 为 0 的临时员工
        allSchedules.stream().filter(schedulesVo -> schedulesVo.getUserId() == 0).forEach(item -> {
            try {
                JSONObject remarkJson = JSONUtil.parseObj(item.getRemark());
                if(ObjectUtil.isAllEmpty(remarkJson)) return;

                String externalEmployeeName = remarkJson.getStr("employeeName");
                if(employees.stream().anyMatch(e -> e.getUserName().equals(externalEmployeeName))) return;

                String position = remarkJson.getStr("position");
                HrpUserProfileVo externalEmployee = new HrpUserProfileVo();
                externalEmployee.setUserId(0L);
                externalEmployee.setMainStoreId(storeId);
                externalEmployee.setPriorityScore(999L);
                externalEmployee.setUserName(externalEmployeeName);
                if (position != null) {
                    HrpSkillsVo skill = hrpSkillsService.queryById(Long.parseLong(position));
                    externalEmployee.setSkills(Collections.singletonList(skill));
                }
                employees.add(externalEmployee);
            } catch (Exception e) {
                log.error("解析临时员工信息时出错, 排班ID: {}, 备注: {}", item.getId(), item.getRemark(), e);
            }
        });

        // 10. 构建最终DTO并返回
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
