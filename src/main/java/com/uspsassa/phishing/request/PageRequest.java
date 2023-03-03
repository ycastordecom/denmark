package com.uspsassa.phishing.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(name = "PageRequest", description = "分页请求")
public class PageRequest<T> {
    @Schema(name = "pageNum", description = "当前页", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value=1,message = "当前页最少从1开始")
    @NotNull(message = "当前页不能为空")
    private Integer pageNum;
    @Schema(name = "pageSize", description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1,message = "每页条数不能为空")
    private Integer pageSize;
    @NotNull
    @Schema(description = "查询条件,不为空，但里边的参数可以", requiredMode = Schema.RequiredMode.REQUIRED)
    T query;
}
