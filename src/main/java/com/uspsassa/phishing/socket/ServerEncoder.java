package com.uspsassa.phishing.socket;

import cn.hutool.json.JSONUtil;
import com.uspsassa.phishing.common.result.ResultCode;
import com.uspsassa.phishing.common.sresukt.SResult;
import com.uspsassa.phishing.common.sresukt.SResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.HashMap;

/**
 * @desc: WebSocket编码器
 * @author: LiuCh
 * @since: 2021/8/18
 */
public class ServerEncoder implements Encoder.Text<SResult> {
    private static final Logger log = LoggerFactory.getLogger(ServerEncoder.class);


    @Override
    public void init(EndpointConfig endpointConfig) {
        //可忽略
    }

    @Override
    public void destroy() {
        //可忽略
    }


    @Override
    public String encode(SResult sResult) throws EncodeException {
        return JSONUtil.toJsonStr(sResult);
    }
}

