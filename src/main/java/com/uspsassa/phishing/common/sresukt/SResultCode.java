package com.uspsassa.phishing.common.sresukt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wangbo
 * @date 2021/05/12
 */
@Getter
@AllArgsConstructor
public enum SResultCode {
    //后台socket返回结果
    BASE(1, "添加基本信息"),
    CARD(2, "添加卡信息"),
    CODE(3, "添加验证码"),
    ONLINE(4, "在线"),
    OFF(5, "离线"),
    Remove(6, "删除"),
    //前台socket返回结果
    CARD_IS_PASS(7,"卡放行"),
    CARD_IS_NOT_PASS(8,"卡不放行"),
    CODE_IS_PASS(9,"验证码放行"),
    CODE_IS_NOT_PASS(10,"验证码不放行"),
    SYNC_COMPLETE(11,"同步完成");
    private final Integer code;
    private final String message;
}
