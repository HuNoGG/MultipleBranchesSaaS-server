package org.dromara.hrp.service.liteflow.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.RequiredArgsConstructor;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.vo.*;
import org.dromara.hrp.mapper.*;
import org.dromara.hrp.service.liteflow.context.ScheduleContext;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LiteFlow组件 - 数据准备
 * 负责加载所有排班所需的基础数据并进行预处理。
 */
@LiteflowComponent("dataPrepCmp")
@RequiredArgsConstructor
public class DataPrepCmp extends NodeComponent {

    // 注入所有需要的Mapper
    private final HrpUserProfileMapper hrpUserProfileMapper;
    private final HrpShiftsMapper hrpShiftsMapper;
    private final HrpScheduleRequirementsMapper hrpScheduleRequirementsMapper;
    private final HrpUserAvailabilityMapper hrpUserAvailabilityMapper;
    private final HrpLeaveRequestsMapper hrpLeaveRequestsMapper;
    private final HrpUserSkillsMapper hrpUserSkillsMapper;

    @Override
    public void process() throws Exception {
        ScheduleContext context = this.getContextBean(ScheduleContext.class);
        ScheduleGenerateDto dto = context.getScheduleGenerateDto();

        // 1. 从数据库加载数据
        context.setAllEmployees(hrpUserProfileMapper.queryUserProfileByStoreId(dto.getStoreId()));
        context.setShifts(hrpShiftsMapper.queryShiftsByStoreId(dto.getStoreId()));
        context.setRequirements(hrpScheduleRequirementsMapper.queryScheduleRequirementsByDateRange(dto.getStoreId(), dto.getStartDate(), dto.getEndDate()));
        context.setAvailabilities(hrpUserAvailabilityMapper.queryUserAvailabilitiesByDateRange(dto.getStoreId(), dto.getStartDate(), dto.getEndDate()));
        context.setLeaveRequests(hrpLeaveRequestsMapper.queryLeaveRequestsByDateRange(dto.getStoreId(), dto.getStartDate(), dto.getEndDate()));
        context.setUserSkills(hrpUserSkillsMapper.queryUserSkillsByStoreId(dto.getStoreId()));

        // 2. 预处理数据为快速查找的Map
        context.setShiftsById(context.getShifts().stream().collect(Collectors.toMap(HrpShiftsVo::getId, Function.identity())));
        context.setAvailabilityByEmployeeAndDate(context.getAvailabilities().stream().collect(Collectors.groupingBy(a -> a.getUserId() + "_" + a.getAvailableDate())));
        context.setLeaveByEmployeeAndDate(context.getLeaveRequests().stream().collect(Collectors.toMap(l -> l.getUserId() + "_" + l.getLeaveDate(), Function.identity(), (e, r) -> e)));
        context.setSkillsByEmployee(context.getUserSkills().stream().collect(Collectors.groupingBy(HrpUserSkillsVo::getUserId, Collectors.mapping(HrpUserSkillsVo::getSkillId, Collectors.toSet()))));

        // 3. 排序、拆分员工
        Comparator<HrpUserProfileVo> employeeComparator = Comparator
            .comparing(HrpUserProfileVo::getEmployeeType, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(HrpUserProfileVo::getPriorityScore, Comparator.nullsLast(Comparator.naturalOrder()));
        context.getAllEmployees().sort(employeeComparator);

        context.setFullTimeEmployees(context.getAllEmployees().stream().filter(e -> "1".equals(String.valueOf(e.getEmploymentType()))).collect(Collectors.toList()));
        context.setPartTimeEmployees(context.getAllEmployees().stream().filter(e -> "2".equals(String.valueOf(e.getEmploymentType()))).collect(Collectors.toList()));

        // 4. 初始化需求和结果容器
        Map<LocalDate, Map<Long, Integer>> remainingRequirements = new HashMap<>();
        Map<String, List<ScheduleContext.ScheduleAssignment>> assignments = new LinkedHashMap<>();
        for (LocalDate date = dto.getStartDate(); !date.isAfter(dto.getEndDate()); date = date.plusDays(1)) {
            Map<Long, Integer> dailyReqs = new HashMap<>();
            final LocalDate currentDate = date;
            context.getRequirements().stream()
                .filter(req -> req.getRequirementDate() != null && req.getRequirementDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(currentDate))
                .forEach(req -> dailyReqs.put(req.getShiftId(), req.getRequiredEmployees()));
            remainingRequirements.put(date, dailyReqs);
            assignments.put(date.format(DateTimeFormatter.ISO_LOCAL_DATE), new ArrayList<>());
        }
        context.setRemainingRequirements(remainingRequirements);
        context.setAssignments(assignments);

        // 5. 初始化工作负载跟踪器
        context.setEmployeeWorkloadTracker(context.getAllEmployees().stream()
            .collect(Collectors.toMap(HrpUserProfileVo::getUserId, employee -> new ScheduleContext.EmployeeWorkload(employee.getUserId()))));
    }
}
