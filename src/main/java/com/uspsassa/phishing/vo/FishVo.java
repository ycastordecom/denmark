package com.uspsassa.phishing.vo;

import com.uspsassa.phishing.entity.Fish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "FishVo", description = "信息")
public class FishVo extends Fish {
    @Schema(description = "是否在线")
    private Boolean isOnline;
}
