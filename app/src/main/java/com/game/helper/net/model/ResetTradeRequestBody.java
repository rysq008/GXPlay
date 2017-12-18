package com.game.helper.net.model;

public class ResetTradeRequestBody extends BaseRequestBody {

    public String passwd;
    public String code;

    public ResetTradeRequestBody(String passwd, String code) {
        super(1);
        this.passwd = passwd;
        this.code = code;
    }
}
