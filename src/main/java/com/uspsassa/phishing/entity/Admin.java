package com.uspsassa.phishing.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class Admin {
    /**
     * 管理员id
     */
    @Schema(description = "管理员id")
    private Integer id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String account;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}