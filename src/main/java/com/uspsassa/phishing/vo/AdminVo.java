package com.uspsassa.phishing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AdminVo", description = "管理员")
public class AdminVo {
    /**
     * 账号
     */
    @Schema(description="账号")
    private String account;
    @Schema(description="Token")
    private String token;
}
