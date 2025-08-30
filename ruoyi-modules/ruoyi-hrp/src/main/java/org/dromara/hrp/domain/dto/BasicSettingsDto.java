package org.dromara.hrp.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class BasicSettingsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long storeId;
    private String crossDayRule;
    private List<ShiftDto> shifts;

    @Data
    public static class ShiftDto {
        private Long id; // 如果id为null或0，则为新增；否则为更新
        private String name;
        private String code;
        private String startTime;
        private String endTime;
        private boolean isCrossDay;
        private String colorCode;
        private List<BreakDto> breakTimes;
    }

    @Data
    public static class BreakDto {
        private Long id;
        private String[] range; // ["12:00", "13:00"]
    }
}
