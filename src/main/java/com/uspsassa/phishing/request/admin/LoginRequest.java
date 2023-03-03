package com.uspsassa.phishing.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Schema(name = "LoginRequest", description = "登录请求")
public class LoginRequest {
    /**
     * 账号
     */
    @Schema(description="账号")
    @NotEmpty(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @Schema(description="密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
