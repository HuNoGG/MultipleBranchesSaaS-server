package org.dromara.hrp.service.impl;

import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.domain.dto.FeedbackItem;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.vo.*;
import org.dromara.hrp.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * SchedulingAlgorithmService 单元测试类
 *
 * @description 使用 Mockito 模拟 Mapper 层，专注于测试排班核心业务逻辑。
 */
@ExtendWith(MockitoExtension.class) // 【修复】使用 JUnit 5 的 Mockito 扩展
class SchedulingAlgorithmServiceTest {

    @InjectMocks
    private SchedulingAlgorithmService schedulingAlgorithmService;

    // --- Mock 所有的 Mapper 依赖 ---
    @Mock private HrpUserProfileMapper userProfileMapper;
    @Mock private HrpUserSkillsMapper userSkillsMapper;
    @Mock private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Mock private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Mock private HrpShiftsMapper shiftsMapper;
    @Mock private HrpSchedulesMapper schedulesMapper;
    @Mock private HrpSkillsMapper skillsMapper;
    @Mock private HrpShiftBreaksMapper shiftBreaksMapper;

    // --- 共享的测试数据 ---
    private List<HrpShiftsVo> mockShifts;
    private List<HrpSkills> mockSkills;

    @BeforeEach
    void setUp() {
        // 在每个测试开始前，初始化通用的模拟数据
        setupMockSkills();
        setupMockShifts();
    }

    /**
     * 场景一：基础全职排班
     * 目标：验证在人力充足的情况下，算法能优先选择全职员工填满所有岗位。
     */
    @Test
    @DisplayName("场景一：基础全职排班 - 人力充足")
    void testBasicFullTimeScheduling() {
        // --- Arrange (准备数据) ---
        LocalDate testDate = LocalDate.now();
        Long storeId = 1L;

        // 模拟员工数据
        List<HrpUserProfileVo> employees = List.of(
            // 全职咖啡师 (高优先级)
            createEmployee(101L, "张三", "全职", 1),
            // 全职收银员 (高优先级)
            createEmployee(102L, "李四", "全职", 1),
            // 兼职收银员 (低优先级)
            createEmployee(201L, "王五", "兼职", 10)
        );

        // 模拟技能数据
        Map<Long, List<Long>> userSkills = Map.of(
            101L, List.of(1L), // 张三 - 咖啡师
            102L, List.of(2L), // 李四 - 收银员
            201L, List.of(2L)  // 王五 - 收银员
        );

        // 模拟可用性 (全职员工全天可用)
        Map<Long, List<HrpUserAvailabilityVo>> availabilities = createFullAvailability(employees, testDate);

        // 模拟排班需求: 早班需要1名咖啡师和1名收银员
        ScheduleGenerateDto dto = createSingleDayDto(storeId, testDate, employees, "09:00-17:00", Map.of("咖啡师", 1, "收银员", 1));

        // Mock Mapper 的行为
        when(userProfileMapper.selectVoByUserIds(anyList())).thenReturn(employees);
        when(userSkillsMapper.selectVoListByUserIds(anyList())).thenReturn(flattenSkills(userSkills));
        when(userAvailabilityMapper.selectVoListByUserIds(anyList())).thenReturn(flattenAvailabilities(availabilities));
        when(leaveRequestsMapper.selectVoListByUsersAndDate(anyList(), any(), any())).thenReturn(Collections.emptyList());
        doNothing().when(schedulesMapper).insertBatch(anyList());

        // --- Act (执行算法) ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- Assert (断言结果) ---
        assertNotNull(result);
        assertEquals(2, result.getSchedules().size(), "应生成两条排班记录");
        assertTrue(result.getFeedbackItems().stream().noneMatch(f -> f.getType() == FeedbackItem.FeedbackType.MANPOWER_SHORTAGE), "不应有人力缺口");

        // 验证排班的员工是否正确
        Set<Long> scheduledUserIds = result.getSchedules().stream().map(HrpSchedules::getUserId).collect(Collectors.toSet());
        assertTrue(scheduledUserIds.contains(101L), "全职咖啡师张三应被排班");
        assertTrue(scheduledUserIds.contains(102L), "全职收银员李四应被排班");
        assertFalse(scheduledUserIds.contains(201L), "兼职收银员王五不应被排班，因为全职已满足需求");
    }

    /**
     * 场景二：兼职补充
     * 目标：验证当全职员工因请假无法满足需求时，算法能自动用合适的兼职员工补上。
     */
    @Test
    @DisplayName("场景二：兼职补充 - 全职员工请假")
    void testPartTimeSupplement() {
        // --- Arrange (准备数据) ---
        LocalDate testDate = LocalDate.now();
        Long storeId = 1L;

        List<HrpUserProfileVo> employees = List.of(
            createEmployee(101L, "张三", "全职", 1), // 全职咖啡师
            createEmployee(102L, "李四", "全职", 1), // 全职收银员 (请假)
            createEmployee(201L, "王五", "兼职", 10)  // 兼职收银员 (无可用性设置 = 全天可用)
        );

        Map<Long, List<Long>> userSkills = Map.of(101L, List.of(1L), 102L, List.of(2L), 201L, List.of(2L));

        // 全职李四请假
        List<HrpLeaveRequestsVo> leaveRequests = List.of(createLeaveRequest(102L, testDate));
        // 兼职王五没有设置可用时间
        Map<Long, List<HrpUserAvailabilityVo>> availabilities = createFullAvailability(List.of(employees.get(0)), testDate);

        ScheduleGenerateDto dto = createSingleDayDto(storeId, testDate, employees, "09:00-17:00", Map.of("咖啡师", 1, "收银员", 1));

        when(userProfileMapper.selectVoByUserIds(anyList())).thenReturn(employees);
        when(userSkillsMapper.selectVoListByUserIds(anyList())).thenReturn(flattenSkills(userSkills));
        when(userAvailabilityMapper.selectVoListByUserIds(anyList())).thenReturn(flattenAvailabilities(availabilities));
        when(leaveRequestsMapper.selectVoListByUsersAndDate(anyList(), any(), any())).thenReturn(leaveRequests);
        doNothing().when(schedulesMapper).insertBatch(anyList());

        // --- Act (执行算法) ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- Assert (断言结果) ---
        assertNotNull(result);
        assertEquals(2, result.getSchedules().size(), "应生成两条排班记录");
        assertTrue(result.getFeedbackItems().stream().noneMatch(f -> f.getType() == FeedbackItem.FeedbackType.MANPOWER_SHORTAGE), "不应有人力缺口");

        Set<Long> scheduledUserIds = result.getSchedules().stream().map(HrpSchedules::getUserId).collect(Collectors.toSet());
        assertTrue(scheduledUserIds.contains(101L), "全职咖啡师张三应被排班");
        assertFalse(scheduledUserIds.contains(102L), "请假的全职收银员李四不应被排班");
        assertTrue(scheduledUserIds.contains(201L), "兼职收银员王五应被用来补充空缺");
    }

    /**
     * 场景三：复杂需求与人力缺口
     * 目标：验证在复杂的多班次、多技能需求下，算法能正确处理并报告无法满足的人力缺口。
     */
    @Test
    @DisplayName("场景三：复杂需求与人力缺口")
    void testComplexSchedulingWithShortage() {
        // --- Arrange (准备数据) ---
        LocalDate testDate = LocalDate.now();
        Long storeId = 1L;

        List<HrpUserProfileVo> employees = List.of(
            createEmployee(101L, "张三", "全职", 1), // 全职咖啡师
            createEmployee(102L, "李四", "全职", 2), // 全职收银员
            createEmployee(201L, "王五", "兼职", 10)  // 兼职咖啡师 (仅晚上可用)
        );

        Map<Long, List<Long>> userSkills = Map.of(101L, List.of(1L), 102L, List.of(2L), 201L, List.of(1L));

        // 兼职王五仅晚上17:00-22:00可用
        Map<Long, List<HrpUserAvailabilityVo>> availabilities = new HashMap<>();
        availabilities.put(101L, List.of(createAvailability(101L, testDate, "00:00", "23:59")));
        availabilities.put(102L, List.of(createAvailability(102L, testDate, "00:00", "23:59")));
        availabilities.put(201L, List.of(createAvailability(201L, testDate, "17:00", "22:00")));

        // 构造多班次需求
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(storeId);
        dto.setStartDate(testDate);
        dto.setEndDate(testDate);
        dto.setEmployees(employees.stream().map(e -> new ScheduleGenerateDto.EmployeeConfig(e.getUserId(), null)).collect(Collectors.toList()));
        Map<String, Map<String, Map<String, Integer>>> requirementsByDay = new HashMap<>();
        Map<String, Map<String, Integer>> dayReq = new HashMap<>();
        dayReq.put("09:00-17:00", Map.of("咖啡师", 1, "收银员", 1)); // 早班需求
        dayReq.put("17:00-22:00", Map.of("咖啡师", 2));             // 晚班需求 (需要2名咖啡师)
        requirementsByDay.put(testDate.toString(), dayReq);
        dto.setRequirementsByDay(requirementsByDay);


        when(userProfileMapper.selectVoByUserIds(anyList())).thenReturn(employees);
        when(userSkillsMapper.selectVoListByUserIds(anyList())).thenReturn(flattenSkills(userSkills));
        when(userAvailabilityMapper.selectVoListByUserIds(anyList())).thenReturn(flattenAvailabilities(availabilities));
        when(leaveRequestsMapper.selectVoListByUsersAndDate(anyList(), any(), any())).thenReturn(Collections.emptyList());
        doNothing().when(schedulesMapper).insertBatch(anyList());

        // --- Act (执行算法) ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- Assert (断言结果) ---
        assertNotNull(result);
        assertEquals(3, result.getSchedules().size(), "应生成三条排班记录");

        // 验证早班排班
        long morningShiftId = getShiftIdByName("09:00-17:00");
        assertTrue(result.getSchedules().stream().anyMatch(s -> s.getUserId().equals(101L) && s.getShiftId().equals(morningShiftId)), "早班应排全职咖啡师张三");
        assertTrue(result.getSchedules().stream().anyMatch(s -> s.getUserId().equals(102L) && s.getShiftId().equals(morningShiftId)), "早班应排全职收银员李四");

        // 验证晚班排班
        long eveningShiftId = getShiftIdByName("17:00-22:00");
        assertTrue(result.getSchedules().stream().anyMatch(s -> s.getUserId().equals(201L) && s.getShiftId().equals(eveningShiftId)), "晚班应排兼职咖啡师王五");

        // 验证人力缺口报告
        assertEquals(1, result.getFeedbackItems().size(), "应有一条反馈信息");
        FeedbackItem feedback = result.getFeedbackItems().get(0);
        assertEquals(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE, feedback.getType());
        assertTrue(feedback.getMessage().contains("晚班") && feedback.getMessage().contains("咖啡师") && feedback.getMessage().contains("缺少 1 人"));
    }


    // ================== 测试数据生成辅助方法 ==================

    private void setupMockSkills() {
        mockSkills = List.of(
            new HrpSkills(1L, "咖啡师"),
            new HrpSkills(2L, "收银员"),
            new HrpSkills(3L, "外场")
        );
        when(skillsMapper.selectList()).thenReturn(mockSkills);
        when(skillsMapper.selectById(1L)).thenReturn(mockSkills.get(0));
        when(skillsMapper.selectById(2L)).thenReturn(mockSkills.get(1));
    }

    private void setupMockShifts() {
        mockShifts = List.of(
            createShift(1L, "早班", "09:00", "17:00"),
            createShift(2L, "晚班", "17:00", "22:00")
        );
        when(shiftsMapper.selectVoListByStoreId(any())).thenReturn(mockShifts);
    }

    private HrpUserProfileVo createEmployee(Long id, String name, String type, Integer priority) {
        HrpUserProfileVo employee = new HrpUserProfileVo();
        employee.setUserId(id);
        employee.setUserName(name);
        // TODO: 这两个是需要你在 HrpUserProfileVo 中添加的字段
        employee.setEmployeeType(type);
        employee.setPriorityScore(Long.valueOf(priority));
        return employee;
    }

    private HrpLeaveRequestsVo createLeaveRequest(Long userId, LocalDate date) {
        HrpLeaveRequestsVo leave = new HrpLeaveRequestsVo();
        leave.setUserId(userId);
        leave.setLeaveDate(date);
        leave.setApprovalStatus("已提交");
        return leave;
    }

    private HrpShiftsVo createShift(Long id, String name, String start, String end) {
        HrpShiftsVo shift = new HrpShiftsVo();
        shift.setId(id);
        shift.setName(name);
        shift.setStartTime(Date.from(LocalTime.parse(start).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()));
        shift.setEndTime(Date.from(LocalTime.parse(end).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()));
        shift.setIsCrossDay(false);
        return shift;
    }

    private HrpUserAvailabilityVo createAvailability(Long userId, LocalDate date, String start, String end) {
        HrpUserAvailabilityVo avail = new HrpUserAvailabilityVo();
        avail.setUserId(userId);
        avail.setDayOfWeek(date.getDayOfWeek().getValue());
        avail.setStartTime(Date.from(LocalTime.parse(start).atDate(date).atZone(ZoneId.systemDefault()).toInstant()));
        avail.setEndTime(Date.from(LocalTime.parse(end).atDate(date).atZone(ZoneId.systemDefault()).toInstant()));
        return avail;
    }

    private Map<Long, List<HrpUserAvailabilityVo>> createFullAvailability(List<HrpUserProfileVo> employees, LocalDate date) {
        Map<Long, List<HrpUserAvailabilityVo>> availabilities = new HashMap<>();
        employees.forEach(e -> availabilities.put(e.getUserId(), List.of(createAvailability(e.getUserId(), date, "00:00", "23:59"))));
        return availabilities;
    }

    private ScheduleGenerateDto createSingleDayDto(Long storeId, LocalDate date, List<HrpUserProfileVo> employees, String timeSlot, Map<String, Integer> skillCounts) {
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(storeId);
        dto.setStartDate(date);
        dto.setEndDate(date);
        dto.setEmployees(employees.stream().map(e -> new ScheduleGenerateDto.EmployeeConfig(e.getUserId(), null)).collect(Collectors.toList()));
        Map<String, Map<String, Map<String, Integer>>> requirementsByDay = new HashMap<>();
        Map<String, Map<String, Integer>> dayReq = new HashMap<>();
        dayReq.put(timeSlot, skillCounts);
        requirementsByDay.put(date.toString(), dayReq);
        dto.setRequirementsByDay(requirementsByDay);
        return dto;
    }

    private List<HrpUserSkillsVo> flattenSkills(Map<Long, List<Long>> userSkillsMap) {
        List<HrpUserSkillsVo> list = new ArrayList<>();
        userSkillsMap.forEach((userId, skillIds) -> {
            skillIds.forEach(skillId -> {
                HrpUserSkillsVo vo = new HrpUserSkillsVo();
                vo.setUserId(userId);
                vo.setSkillId(skillId);
                list.add(vo);
            });
        });
        return list;
    }

    private List<HrpUserAvailabilityVo> flattenAvailabilities(Map<Long, List<HrpUserAvailabilityVo>> availabilitiesMap) {
        return availabilitiesMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    private Long getShiftIdByName(String timeSlot) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return mockShifts.stream()
            .filter(s -> timeSlot.equals(s.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter) + "-" + s.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)))
            .map(HrpShiftsVo::getId)
            .findFirst()
            .orElse(-1L);
    }
}
