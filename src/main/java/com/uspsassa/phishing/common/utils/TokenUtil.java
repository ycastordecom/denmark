package com.uspsassa.phishing.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.uspsassa.phishing.entity.Setting;
import com.uspsassa.phishing.exception.AuthException;
import com.uspsassa.phishing.service.SettingService;
import com.uspsassa.phishing.vo.AdminVo;

import javax.servlet.http.HttpServletResponse;


public class TokenUtil {
    //获取token
    public static String getToken(AdminVo adminVo) {
        Setting setting = SpringUtil.getBean(SettingService.class).getSetting();
        return JWT.create().setNotBefore(DateUtil.date())
                .setExpiresAt(DateUtil.offsetMinute(DateUtil.date(), 30))
                .setPayload("username", adminVo.getAccount())
                .setKey(setting.getTokenKey().getBytes())
                .sign();
    }

    //验证token
    public static void verify(String token, HttpServletResponse response) {
        if (token == null|| "".equals(token)) {
            throw new AuthException("您还未登录");
        }
        Setting setting = SpringUtil.getBean(SettingService.class).getSetting();
        //验证是否已过期
        try {
            JWT.of(token).setKey(setting.getTokenKey().getBytes()).validate(0);
        }catch (Exception e){
            throw new AuthException("Token无效，请重新登录");
        }
        //如果失效时间小于5分钟，生效时间加30分钟
        if ((Integer) JWT.of(token).setKey("123456".getBytes()).getPayload(JWTPayload.EXPIRES_AT) - DateUtil.currentSeconds() < 300) {
            JWT jwt = JWT.of(token).setKey("123456".getBytes());
            jwt.setPayload("exp", DateUtil.currentSeconds() + 1800);
            String newToken = jwt.sign();
            response.setHeader("Authorization", newToken);
        }
    }
}
