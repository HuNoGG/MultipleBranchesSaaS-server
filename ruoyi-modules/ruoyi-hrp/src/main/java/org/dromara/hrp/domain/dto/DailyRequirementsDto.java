package org.dromara.hrp.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class DailyRequirementsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "店铺ID不能为空")
    private Long storeId;

    @NotBlank(message = "日期类型不能为空")
    private String dayType; // "weekday" or "holiday"

    private List<RequirementItem> requirements;

    @Data
    public static class RequirementItem {
        @NotNull
        private Long shiftId;
        @NotNull
        private Long skillId;
        @NotNull
        private Integer requiredCount;
    }
}
