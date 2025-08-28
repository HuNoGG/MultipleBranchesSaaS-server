package org.dromara.hrp.service.impl;

import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.domain.HrpStores;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 智能排班算法核心服务 SchedulingAlgorithmService 的单元测试类 (已修复 @MockBean 弃用问题)
 *
 * @Author Gemini AI Assistant
 * @Version 1.1
 */
@ExtendWith(MockitoExtension.class) // 【修复】使用 JUnit 5 的 Mockito 扩展
class SchedulingAlgorithmServiceTest {

    // 【修复】将 @MockBean 全部替换为 @Mock
    @Mock private HrpUserProfileMapper userProfileMapper;
    @Mock private HrpUserSkillsMapper userSkillsMapper;
    @Mock private HrpUserAvailabilityMapper userAvailabilityMapper;
    @Mock private HrpLeaveRequestsMapper leaveRequestsMapper;
    @Mock private HrpScheduleRequirementsMapper scheduleRequirementsMapper;
    @Mock private HrpShiftsMapper shiftsMapper;
    @Mock private HrpSchedulesMapper schedulesMapper;
    @Mock private HrpStoreEventsMapper storeEventsMapper;
    @Mock private HrpSkillsMapper skillsMapper;
    @Mock private HrpStoresMapper storesMapper;
    @Mock private HrpShiftBreaksMapper shiftBreaksMapper;

    // @InjectMocks 会将上面声明的 @Mock 对象自动注入到被测试的服务实例中
    @InjectMocks
    private SchedulingAlgorithmService schedulingAlgorithmService;

    // --- 通用测试数据 ---
    private final Long STORE_ID = 1L;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private List<HrpSkills> allSkills;
    private HrpStores defaultStore;

    /**
     * 在每个测试方法执行前运行，用于初始化通用的模拟数据和 Mockito 行为
     */
    @BeforeEach
    void setUp() {
        // 初始化所有技能/岗位
        allSkills = Arrays.asList(
            createSkill(101L, "出锅"),
            createSkill(102L, "带位"),
            createSkill(103L, "外场")
        );
        // 模拟 skillsMapper 的行为
        Mockito.when(skillsMapper.selectList()).thenReturn(allSkills);

        // 模拟一个默认的分店设置
        defaultStore = new HrpStores();
        defaultStore.setId(STORE_ID);
        defaultStore.setCrossDayRule("by_shift_start"); // 默认按班别开始日计算工时
        Mockito.when(storesMapper.selectById(STORE_ID)).thenReturn(defaultStore);
    }


    @Test
    @DisplayName("【基本场景】测试 - 正常情况下应成功生成排班且无错误")
    void testGenerateSchedule_BasicScenario() throws ParseException {
        // --- 1. 准备模拟数据 ---

        // 模拟员工档案
        List<HrpUserProfileVo> employees = Arrays.asList(
            createUser(1L, "张三", "全职", 100L),
            createUser(2L, "李四", "计时", 80L)
        );
        Mockito.when(userProfileMapper.selectVoByUserIds(Arrays.asList(1L, 2L))).thenReturn(employees);

        // 模拟员工技能
        List<HrpUserSkillsVo> skills = Arrays.asList(
            createUserSkill(1L, 101L), // 张三: 出锅
            createUserSkill(1L, 102L), // 张三: 带位
            createUserSkill(2L, 103L)  // 李四: 外场
        );
        Mockito.when(userSkillsMapper.selectVoListByUserIds(Arrays.asList(1L, 2L))).thenReturn(skills);

        // 模拟班次
        List<HrpShiftsVo> shifts = Collections.singletonList(
            createShift(201L, "全天班", "10:00", "18:00", false)
        );
        Mockito.when(shiftsMapper.selectVoListByStoreId(STORE_ID)).thenReturn(shifts);

        // 模拟空数据（无休假、无特殊可用时间等）
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        Mockito.when(leaveRequestsMapper.selectVoListByUsersAndDate(Mockito.anyList(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Collections.emptyList());
        Mockito.when(userAvailabilityMapper.selectVoListByUserIds(Mockito.anyList())).thenReturn(Collections.emptyList());
        Mockito.when(shiftBreaksMapper.selectList()).thenReturn(Collections.emptyList());

        // --- 2. 准备 DTO (输入) ---
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(STORE_ID);
        dto.setStartDate(startDate);
        dto.setEndDate(LocalDate.of(2025, 9, 1));
        dto.setSchedulingMode("PRIORITY"); // 优先模式
        dto.setEmployees(Arrays.asList(
            createEmployeeConfig(1L, Collections.emptyList()),
            createEmployeeConfig(2L, Collections.emptyList())
        ));
        // 设置人力需求：需要1个出锅，1个外场
        Map<String, Map<String, Integer>> requirements = new HashMap<>();
        Map<String, Integer> shiftReqs = new HashMap<>();
        shiftReqs.put("出锅", 1);
        shiftReqs.put("外场", 1);
        requirements.put("10:00-18:00", shiftReqs);
        dto.setRequirements(requirements);

        // --- 3. 执行算法 ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- 4. 验证结果 (断言) ---
        assertNotNull(result, "返回结果不应为null");
        assertEquals(2, result.getSchedules().size(), "应生成2条排班记录");
        assertTrue(result.getFeedbackItems().isEmpty(), "不应有任何反馈信息");
    }


    @Test
    @DisplayName("【人力缺口】测试 - 员工不足时应准确报告缺口")
    void testGenerateSchedule_ManpowerShortage() {
        // --- 1. 数据 ---
        List<HrpUserProfileVo> employees = Collections.singletonList(createUser(1L, "张三", "全职", 100L));
        Mockito.when(userProfileMapper.selectVoByUserIds(Collections.singletonList(1L))).thenReturn(employees);
        List<HrpUserSkillsVo> skills = Arrays.asList(createUserSkill(1L, 101L), createUserSkill(1L, 102L));
        Mockito.when(userSkillsMapper.selectVoListByUserIds(Collections.singletonList(1L))).thenReturn(skills);
        List<HrpShiftsVo> shifts = Collections.singletonList(createShift(201L, "全天班", "10:00", "18:00", false));
        HrpSkills skill = createSkill(102L, "带位");
        Mockito.when(skillsMapper.selectById(102L)).thenReturn(skill);
        Mockito.when(shiftsMapper.selectVoListByStoreId(STORE_ID)).thenReturn(shifts);

        // --- 2. DTO ---
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(STORE_ID);
        dto.setStartDate(LocalDate.of(2025, 9, 1));
        dto.setEndDate(LocalDate.of(2025, 9, 1));
        dto.setEmployees(Collections.singletonList(createEmployeeConfig(1L, Collections.emptyList())));
        // 需求：需要一个“出锅”和一个“带位”，但没有人会“带位”
        Map<String, Map<String, Integer>> requirements = new HashMap<>();
        Map<String, Integer> shiftReqs = new HashMap<>();
        shiftReqs.put("出锅", 1);
        shiftReqs.put("带位", 1); // 这个岗位将会缺人
        requirements.put("10:00-18:00", shiftReqs);
        dto.setRequirements(requirements);

        // --- 3. 执行 ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- 4. 验证 ---
        assertNotNull(result);
        assertEquals(1, result.getSchedules().size());
        assertEquals(1, result.getFeedbackItems().size());

        FeedbackItem feedback = result.getFeedbackItems().get(0);
        assertEquals(FeedbackItem.FeedbackType.MANPOWER_SHORTAGE, feedback.getType());
        assertTrue(feedback.getMessage().contains("带位"));
        assertTrue(feedback.getMessage().contains("缺少 1 人"));
    }

    @Test
    @DisplayName("【休假冲突】测试 - 全职休假超限时应自动取消并通知")
    void testGenerateSchedule_FullTimeLeaveConflict() {
        // --- 1. 数据 ---
        List<HrpUserProfileVo> employees = Arrays.asList(
            createUser(1L, "张三", "全职", 100L),
            createUser(2L, "李四", "全职", 90L),
            createUser(3L, "王五", "全职", 80L)
        );
        Mockito.when(userProfileMapper.selectVoByUserIds(Arrays.asList(1L, 2L, 3L))).thenReturn(employees);

        LocalDate conflictDate = LocalDate.of(2025, 9, 2);
        List<HrpLeaveRequestsVo> leaves = Arrays.asList(
            createLeave(1L, conflictDate),
            createLeave(2L, conflictDate),
            createLeave(3L, conflictDate)
        );
        Mockito.when(leaveRequestsMapper.selectVoListByUsersAndDate(Mockito.anyList(), Mockito.any(), Mockito.any())).thenReturn(leaves);

        // --- 2. DTO ---
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(STORE_ID);
        dto.setStartDate(conflictDate);
        dto.setEndDate(conflictDate);
        dto.setEmployees(Arrays.asList(
            createEmployeeConfig(1L, Collections.emptyList()),
            createEmployeeConfig(2L, Collections.emptyList()),
            createEmployeeConfig(3L, Collections.emptyList())
        ));
        dto.setRequirements(new HashMap<>());

        // --- 3. 执行 ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- 4. 验证 ---
        assertNotNull(result);
        assertEquals(1, result.getFeedbackItems().size());

        FeedbackItem feedback = result.getFeedbackItems().get(0);
        assertEquals(FeedbackItem.FeedbackType.SYSTEM_WARNING, feedback.getType());
        assertTrue(feedback.getMessage().contains("休假申请已被系统自动取消"));
    }

    @Test
    @DisplayName("【休息替班】测试 - 应为全职的长时休息安排计时工替班")
    void testGenerateSchedule_BreakCoverage() throws ParseException {
        // --- 1. 数据 ---
        List<HrpUserProfileVo> employees = Arrays.asList(
            createUser(1L, "张三(全职)", "全职", 100L),
            createUser(2L, "李四(计时)", "计时", 80L)
        );
        Mockito.when(userProfileMapper.selectVoByUserIds(Arrays.asList(1L, 2L))).thenReturn(employees);

        List<HrpUserSkillsVo> skills = Arrays.asList(
            createUserSkill(1L, 101L), // 张三: 出锅
            createUserSkill(2L, 101L)  // 李四也会: 出锅
        );
        Mockito.when(userSkillsMapper.selectVoListByUserIds(Arrays.asList(1L, 2L))).thenReturn(skills);

        HrpShiftsVo shiftWithLongBreak = createShift(201L, "全天班", "10:00", "19:00", false);
        Mockito.when(shiftsMapper.selectVoListByStoreId(STORE_ID)).thenReturn(Collections.singletonList(shiftWithLongBreak));

        HrpShiftBreaksVo longBreak = createBreak(301L, 201L, "12:00", "14:00");
        Mockito.when(shiftBreaksMapper.selectVoList()).thenReturn(Collections.singletonList(longBreak));

        // --- 2. DTO ---
        ScheduleGenerateDto dto = new ScheduleGenerateDto();
        dto.setStoreId(STORE_ID);
        dto.setStartDate(LocalDate.of(2025, 9, 3));
        dto.setEndDate(LocalDate.of(2025, 9, 3));
        dto.setEmployees(Arrays.asList(
            createEmployeeConfig(1L, Collections.emptyList()),
            createEmployeeConfig(2L, Collections.emptyList())
        ));
        Map<String, Map<String, Integer>> requirements = new HashMap<>();
        Map<String, Integer> shiftReqs = new HashMap<>();
        shiftReqs.put("出锅", 1);
        requirements.put("10:00-19:00", shiftReqs);
        dto.setRequirements(requirements);

        // --- 3. 执行 ---
        ScheduleGenerationResult result = schedulingAlgorithmService.generateSchedule(dto);

        // --- 4. 验证 ---
        assertNotNull(result);
        assertTrue(result.getFeedbackItems().isEmpty(), "不应有任何缺口或警告");
        assertEquals(2, result.getSchedules().size());

        boolean hasBreakCoverageShift = result.getSchedules().stream()
            .anyMatch(s -> s.getUserId().equals(2L) && s.getShiftId() < 0);
        assertTrue(hasBreakCoverageShift, "必须包含一条计时工的替班记录");
    }

    // --- 辅助方法，用于快速创建模拟对象 ---

    private HrpUserProfileVo createUser(Long id, String name, String type, Long score) {
        HrpUserProfileVo user = new HrpUserProfileVo();
        user.setUserId(id);
        user.setUserName(name);
        user.setEmployeeType(type);
        user.setPriorityScore(score);
        return user;
    }

    private HrpUserSkillsVo createUserSkill(Long userId, Long skillId) {
        HrpUserSkillsVo userSkill = new HrpUserSkillsVo();
        userSkill.setUserId(userId);
        userSkill.setSkillId(skillId);
        return userSkill;
    }

    private HrpShiftsVo createShift(Long id, String name, String start, String end, boolean isCrossDay) {
        try {
            HrpShiftsVo shift = new HrpShiftsVo();
            shift.setId(id);
            shift.setName(name);
            shift.setStartTime(timeFormat.parse(start));
            shift.setEndTime(timeFormat.parse(end));
            shift.setIsCrossDay(isCrossDay ? 1L : 0L);
            return shift;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private HrpShiftBreaksVo createBreak(Long id, Long shiftId, String start, String end) throws ParseException {
        HrpShiftBreaksVo breakItem = new HrpShiftBreaksVo();
        breakItem.setId(id);
        breakItem.setShiftId(shiftId);
        breakItem.setBreakStartTime(timeFormat.parse(start));
        breakItem.setBreakEndTime(timeFormat.parse(end));
        return breakItem;
    }

    private HrpSkills createSkill(Long id, String name) {
        HrpSkills skill = new HrpSkills();
        skill.setId(id);
        skill.setName(name);
        return skill;
    }

    private HrpLeaveRequestsVo createLeave(Long userId, LocalDate date) {
        HrpLeaveRequestsVo leave = new HrpLeaveRequestsVo();
        leave.setUserId(userId);
        leave.setLeaveDate(date);
        leave.setApprovalStatus("已提交");
        return leave;
    }

    private ScheduleGenerateDto.EmployeeConfig createEmployeeConfig(Long id, List<String> daysOff) {
        ScheduleGenerateDto.EmployeeConfig config = new ScheduleGenerateDto.EmployeeConfig();
        config.setId(id);
        config.setDaysOff(daysOff);
        return config;
    }
}
