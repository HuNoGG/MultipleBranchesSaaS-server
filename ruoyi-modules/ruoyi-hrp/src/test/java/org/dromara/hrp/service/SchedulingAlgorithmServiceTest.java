package org.dromara.hrp.service;

import org.dromara.hrp.domain.HrpLeaveRequests;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.vo.*;
import org.dromara.hrp.mapper.*;
import org.dromara.hrp.service.impl.SchedulingAlgorithmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class SchedulingAlgorithmServiceTest {

    @InjectMocks
    private SchedulingAlgorithmService schedulingAlgorithmService;

    @Mock private HrpUserProfileMapper userProfileMapper;
    @Mock private HrpUserSkillsMapper userSkillsMapper;
    @Mock private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Mock private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Mock private HrpShiftsMapper shiftsMapper;
    @Mock private HrpSchedulesMapper schedulesMapper;
    @Mock private HrpSkillsMapper skillsMapper;
    @Mock private HrpShiftBreaksMapper shiftBreaksMapper;

    private HrpUserProfileVo ftEmployee;
    private HrpUserProfileVo ptEmployeeAllDay;
    private HrpUserProfileVo ptEmployeeLimited;
    private HrpUserProfileVo ptEmployeeHighSkill;
    private HrpShiftsVo shift1;
    private HrpSkills skill1;
    private ScheduleGenerateDto baseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        skill1 = new HrpSkills();
        skill1.setId(1L);
        skill1.setName("收银");

        ftEmployee = new HrpUserProfileVo();
        ftEmployee.setUserId(1L);
        ftEmployee.setUserName("FullTimer");
        ftEmployee.setEmployeeType("正职");
        ftEmployee.setPriorityScore(10L);

        ptEmployeeAllDay = new HrpUserProfileVo();
        ptEmployeeAllDay.setUserId(2L);
        ptEmployeeAllDay.setUserName("PartTimerAllDay");
        ptEmployeeAllDay.setEmployeeType("兼职");
        ptEmployeeAllDay.setPriorityScore(10L);

        ptEmployeeLimited = new HrpUserProfileVo();
        ptEmployeeLimited.setUserId(3L);
        ptEmployeeLimited.setUserName("PartTimerLimited");
        ptEmployeeLimited.setEmployeeType("兼职");
        ptEmployeeLimited.setPriorityScore(10L);

        ptEmployeeHighSkill = new HrpUserProfileVo();
        ptEmployeeHighSkill.setUserId(4L);
        ptEmployeeHighSkill.setUserName("PartTimerHighSkill");
        ptEmployeeHighSkill.setEmployeeType("兼职");
        ptEmployeeHighSkill.setPriorityScore(5L);

        shift1 = new HrpShiftsVo();
        shift1.setId(101L);
        shift1.setName("早班");
        shift1.setStartTime(LocalTime.of(9, 0));
        shift1.setEndTime(LocalTime.of(17, 0));
        shift1.setIsCrossDay(false);

        baseDto = new ScheduleGenerateDto();
        baseDto.setStoreId(1L);
        baseDto.setStartDate(LocalDate.of(2025, 1, 6));
        baseDto.setEndDate(LocalDate.of(2025, 1, 6));
        baseDto.setSchedulingMode("PRIORITY");

        Map<String, Integer> skillReq = new HashMap<>();
        skillReq.put(skill1.getName(), 1);
        Map<String, Map<String, Integer>> timeSlotReq = new HashMap<>();
        timeSlotReq.put("09:00-17:00", skillReq);
        Map<String, Map<String, Map<String, Integer>>> dailyReq = new HashMap<>();
        dailyReq.put("2025-01-06", timeSlotReq);
        baseDto.setRequirementsByDay(dailyReq);

        when(shiftsMapper.selectVoListByStoreId(any())).thenReturn(Collections.singletonList(shift1));
        when(shiftBreaksMapper.selectVoList()).thenReturn(Collections.emptyList());
        when(schedulesMapper.selectWorkDatesForUsersBefore(anyList(), any(), any(int.class))).thenReturn(Collections.emptyList());
        when(skillsMapper.selectList()).thenReturn(Collections.singletonList(skill1));
        when(skillsMapper.selectById(1L)).thenReturn(skill1);

        when(userProfileMapper.selectVoByUserIds(anyList())).thenReturn(Collections.emptyList());
        when(userSkillsMapper.selectVoListByUserIds(anyList())).thenReturn(Collections.emptyList());
        when(userAvailabilityMapper.selectVoListByUserIds(anyList())).thenReturn(Collections.emptyList());
        when(leaveRequestsMapper.selectVoListByUsersAndDate(anyList(), any(), any())).thenReturn(Collections.emptyList());
    }

    private void setupEmployeeMocks(List<HrpUserProfileVo> employees) {
        List<Long> userIds = employees.stream().map(HrpUserProfileVo::getUserId).collect(Collectors.toList());
        baseDto.setEmployees(userIds.stream().map(id -> new ScheduleGenerateDto.EmployeeConfig(id, null)).collect(Collectors.toList()));
        when(userProfileMapper.selectVoByUserIds(userIds)).thenReturn(employees);

        HrpUserSkillsVo ftSkill = new HrpUserSkillsVo();
        ftSkill.setUserId(ftEmployee.getUserId()); ftSkill.setSkillId(skill1.getId()); ftSkill.setPriority(1L);
        HrpUserSkillsVo ptAllDaySkill = new HrpUserSkillsVo();
        ptAllDaySkill.setUserId(ptEmployeeAllDay.getUserId()); ptAllDaySkill.setSkillId(skill1.getId()); ptAllDaySkill.setPriority(1L);
        HrpUserSkillsVo ptLimitedSkill = new HrpUserSkillsVo();
        ptLimitedSkill.setUserId(ptEmployeeLimited.getUserId()); ptLimitedSkill.setSkillId(skill1.getId()); ptLimitedSkill.setPriority(1L);
        HrpUserSkillsVo ptHighSkillVo = new HrpUserSkillsVo();
        ptHighSkillVo.setUserId(ptEmployeeHighSkill.getUserId()); ptHighSkillVo.setSkillId(skill1.getId()); ptHighSkillVo.setPriority(10L);
        when(userSkillsMapper.selectVoListByUserIds(userIds)).thenReturn(Arrays.asList(ftSkill, ptAllDaySkill, ptLimitedSkill, ptHighSkillVo));

        HrpUserAvailabilityVo limitedAvail = new HrpUserAvailabilityVo();
        limitedAvail.setUserId(ptEmployeeLimited.getUserId());
        limitedAvail.setDayOfWeek(LocalDate.of(2025, 1, 6).getDayOfWeek().getValue());
        limitedAvail.setStartTime(LocalTime.of(12, 0));
        limitedAvail.setEndTime(LocalTime.of(18, 0));
        when(userAvailabilityMapper.selectVoListByUserIds(userIds)).thenReturn(Collections.singletonList(limitedAvail));
    }


    @Test
    void testPriorityLogic() {
        setupEmployeeMocks(Arrays.asList(ftEmployee, ptEmployeeAllDay, ptEmployeeLimited, ptEmployeeHighSkill));
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(baseDto);
        assertNotNull(result.getSchedules());
        assertEquals(1, result.getSchedules().size());
        assertEquals(ftEmployee.getUserId(), result.getSchedules().get(0).getUserId());

        setupEmployeeMocks(Arrays.asList(ptEmployeeAllDay, ptEmployeeLimited, ptEmployeeHighSkill));
        result = schedulingAlgorithmService.generateSchedule(baseDto);
        assertEquals(1, result.getSchedules().size());
        assertEquals(ptEmployeeHighSkill.getUserId(), result.getSchedules().get(0).getUserId());

        setupEmployeeMocks(Arrays.asList(ptEmployeeAllDay, ptEmployeeLimited));
        result = schedulingAlgorithmService.generateSchedule(baseDto);
        assertEquals(1, result.getSchedules().size());
        assertEquals(ptEmployeeAllDay.getUserId(), result.getSchedules().get(0).getUserId());
    }

    @Test
    void testEmployeeOnLeave() {
        setupEmployeeMocks(Arrays.asList(ftEmployee, ptEmployeeAllDay));
        HrpLeaveRequestsVo leave = new HrpLeaveRequestsVo();
        leave.setUserId(ftEmployee.getUserId());
        leave.setLeaveDate(LocalDate.of(2025, 1, 6));
        leave.setApprovalStatus("已锁定");
        when(leaveRequestsMapper.selectVoListByUsersAndDate(anyList(), any(), any())).thenReturn(Collections.singletonList(leave));

        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(baseDto);

        assertEquals(1, result.getSchedules().size());
        assertEquals(ptEmployeeAllDay.getUserId(), result.getSchedules().get(0).getUserId());
    }

    @Test
    void testConsecutiveWorkDaysWithHistory() {
        // Set max consecutive work days to 5 for this test
        baseDto.setMaxConsecutiveWorkDays(5);
        setupEmployeeMocks(Arrays.asList(ftEmployee, ptEmployeeAllDay));

        // Give the full-time employee 5 consecutive historical work days
        List<HrpSchedulesVo> historicalSchedules = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HrpSchedulesVo schedule = new HrpSchedulesVo();
            schedule.setUserId(ftEmployee.getUserId());
            schedule.setScheduleDate(LocalDate.of(2025, 1, i)); // Jan 1 to Jan 5
            historicalSchedules.add(schedule);
        }
        when(schedulesMapper.selectWorkDatesForUsersBefore(anyList(), any(), any(int.class))).thenReturn(historicalSchedules);

        // Act
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(baseDto);

        // Assert: ftEmployee has worked 5 days straight, cannot work the 6th. ptEmployeeAllDay should be scheduled.
        assertEquals(1, result.getSchedules().size());
        assertEquals(ptEmployeeAllDay.getUserId(), result.getSchedules().get(0).getUserId());
    }
}
