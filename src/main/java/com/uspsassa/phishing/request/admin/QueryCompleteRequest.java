package com.uspsassa.phishing.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "查询完成请求")
@Data
public class QueryCompleteRequest {

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
     * 名称
     */
    @Schema(description = "名称")
    private String name;
}
