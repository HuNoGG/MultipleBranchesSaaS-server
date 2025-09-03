package org.dromara.hrp.service.liteflow.cmp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.dromara.hrp.service.liteflow.context.ScheduleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

/**
 * LiteFlow组件 - 排班校验
 * 检查最终生成的排班是否满足所有硬性需求。
 */
@LiteflowComponent("scheduleValidationCmp")
public class ScheduleValidationCmp extends NodeComponent {
    private static final Logger log = LoggerFactory.getLogger(ScheduleValidationCmp.class);

    @Override
    public void process() throws Exception {
        ScheduleContext context = this.getContextBean(ScheduleContext.class);
        log.info("【排班流程-阶段三】校验最终排班结果...");

        boolean allMet = true;
        for (Map.Entry<LocalDate, Map<Long, Integer>> entry : context.getRemainingRequirements().entrySet()) {
            for (Map.Entry<Long, Integer> dailyEntry : entry.getValue().entrySet()) {
                if (dailyEntry.getValue() > 0) {
                    log.warn("排班校验失败：日期 [{}], 班次 [{}], 仍有 [{}] 个空缺未被满足。",
                        entry.getKey(), dailyEntry.getKey(), dailyEntry.getValue());
                    allMet = false;
                }
            }
        }

        if (allMet) {
            log.info("排班校验通过：所有班次需求均已满足。");
        } else {
            // 注意：这里只记录警告，不中断流程。如果需要中断，可以抛出异常。
            // throw new LiteFlowException("排班校验失败，存在未满足的需求！");
        }
    }
}
