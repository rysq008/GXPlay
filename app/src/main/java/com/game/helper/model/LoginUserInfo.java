package com.game.helper.model;

import java.io.Serializable;

/**
 * Created by sung on 2017/12/1.
 */

public class LoginUserInfo implements Serializable {
    public String phone;
    public int member_id;
    public String icon;
    public boolean has_passwd;//登录密码
    public boolean has_trade_passwd;//交易密码
    public boolean has_alipay_account;//支付宝账号

    public LoginUserInfo() {
    }

    public LoginUserInfo(LoginResults loginResults) {
        if (loginResults == null)
            return;

        this.phone = loginResults.phone;
        this.member_id = loginResults.member_id;
        this.icon = loginResults.icon;
        this.has_passwd = loginResults.has_passwd;
        this.has_trade_passwd = loginResults.has_trade_passwd;
        this.has_alipay_account = loginResults.has_alipay_account;
    }

    public LoginUserInfo(RegistResults registResults) {
        if (registResults == null)
            return;

        this.icon = registResults.icon;
        this.phone = registResults.phone;
        this.member_id = registResults.member_id;
        this.has_passwd = registResults.has_passwd;
        this.has_trade_passwd = registResults.has_trade_passwd;
        this.has_alipay_account = registResults.has_alipay_account;
    }

    public LoginUserInfo(String phone, int member_id, String icon, boolean has_passwd, boolean has_trade_passwd, boolean has_alipay_account) {
        this.phone = phone;
        this.member_id = member_id;
        this.icon = icon;
        this.has_passwd = has_passwd;
        this.has_trade_passwd = has_trade_passwd;
        this.has_alipay_account = has_alipay_account;
    }
}
