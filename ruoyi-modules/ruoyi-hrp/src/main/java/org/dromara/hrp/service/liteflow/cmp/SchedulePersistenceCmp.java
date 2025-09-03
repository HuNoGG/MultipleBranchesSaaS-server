package org.dromara.hrp.service.liteflow.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.RequiredArgsConstructor;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.mapper.HrpSchedulesMapper;
import org.dromara.hrp.service.liteflow.context.ScheduleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * LiteFlow组件 - 结果持久化
 * 将最终生成的排班结果保存到数据库。
 */
@LiteflowComponent("schedulePersistenceCmp")
@RequiredArgsConstructor
public class SchedulePersistenceCmp extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(SchedulePersistenceCmp.class);

    private final HrpSchedulesMapper hrpSchedulesMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 在组件级别启用事务
    public void process() throws Exception {
        ScheduleContext context = this.getContextBean(ScheduleContext.class);
        log.info("【排班流程-阶段四】开始持久化排班结果...");

        ScheduleGenerateDto dto = context.getScheduleGenerateDto();

        // 1. 删除指定日期范围内的旧排班
        hrpSchedulesMapper.deleteSchedulesByDateRange(dto.getStoreId(), dto.getStartDate(), dto.getEndDate());
        log.debug("已删除门店[{}]在[{} - {}]范围内的旧排班。", dto.getStoreId(), dto.getStartDate(), dto.getEndDate());

        // 2. 构造新的排班数据列表
        List<HrpSchedules> newSchedules = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        context.getAssignments().forEach((dateStr, assignmentList) -> {
            LocalDate scheduleDate = LocalDate.parse(dateStr, formatter);
            for (ScheduleContext.ScheduleAssignment assignment : assignmentList) {
                HrpSchedules schedule = new HrpSchedules();
                schedule.setStoreId(dto.getStoreId());
                schedule.setUserId(assignment.getEmployeeId());
                schedule.setShiftId(assignment.getShiftId());
                schedule.setScheduleDate(scheduleDate);

                HrpShiftsVo shift = context.getShiftsById().get(assignment.getShiftId());
                if (shift != null) {
                    schedule.setStartTime(shift.getStartTime());
                    schedule.setEndTime(shift.getEndTime());
                }
                schedule.setCreateTime(new Date());
                // TODO: createBy 需要从当前用户安全上下文中获取
                newSchedules.add(schedule);
            }
        });

        // 3. 批量插入新排班
        if (!newSchedules.isEmpty()) {
            hrpSchedulesMapper.insertBatch(newSchedules);
            log.info("成功持久化 {} 条新排班记录。", newSchedules.size());
        } else {
            log.warn("没有生成任何排班记录，无需持久化。");
        }
    }
}
