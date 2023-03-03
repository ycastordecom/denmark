package com.uspsassa.phishing.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wangbo
 * @date 2021/05/12
 */
@Data
//@Schema(name = "Result",description = "响应结果")
@Schema(description = "响应结果")
public class Result<T> {
    @Schema(description = "响应码")
    private Integer code;

    @Schema(description = "响应信息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    /**
     * 成功
     */
    public static Result<Void> success() {
        Result<Void> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }

    /**
     * 成功，有返回数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.getCode();
        result.message = ResultCode.SUCCESS.getMessage();
        result.data = data;
        return result;
    }

    /**
     * 失败，自定义失败信息
     */
    public static Result<Void> failure(Integer code,String message) {
        Result<Void> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败，使用已定义枚举
     */
    public static Result<Void> failure(ResultCode resultCode) {
        Result<Void> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }
}
