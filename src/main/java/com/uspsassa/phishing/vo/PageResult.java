package com.uspsassa.phishing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "PageResult",description = "分页结果")
public class PageResult<T> {
    @Schema(name = "hasNext",description = "是否存在下一页")
    private Boolean hasNext;
    @Schema(name = "total",description = "总条数")
    private Long total;
    @Schema(name = "data",description = "数据")
    private List<T> data;
}
