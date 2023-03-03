package com.uspsassa.phishing.common.sresukt;

import com.uspsassa.phishing.common.result.Result;
import com.uspsassa.phishing.common.result.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "socket响应结果")
public class SResult <T>{
    private Integer code;
    private T data;
    public static <T> SResult<T> success(SResultCode sResultCode, T data) {
        SResult<T> result = new SResult<>();
        result.setCode(sResultCode.getCode());
        result.setData(data);
        return result;
    }
}
