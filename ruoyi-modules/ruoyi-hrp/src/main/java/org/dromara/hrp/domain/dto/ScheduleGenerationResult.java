package org.dromara.hrp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hrp.domain.HrpSchedules;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleGenerationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 生成的排班记录
     */
    private List<HrpSchedules> schedules;

    /**
     * 执行过程中的结构化反馈信息
     */
    private List<FeedbackItem> feedbackItems;
}
