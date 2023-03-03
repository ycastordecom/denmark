package com.uspsassa.phishing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "SystemCount", description = "系统统计")
@Data
public class SystemCount {
    @Schema(description = "访问量")
    private Integer visitCount;
    @Schema(description = "完成量")
    private Integer finishCount;
}
