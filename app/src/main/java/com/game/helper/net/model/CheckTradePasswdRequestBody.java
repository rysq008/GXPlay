package com.game.helper.net.model;

public class CheckTradePasswdRequestBody extends BaseRequestBody {
    private String trade_passwd;

    public CheckTradePasswdRequestBody(String tradePassword) {
        super(1);
        this.trade_passwd = tradePassword;
    }
}
