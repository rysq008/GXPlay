package com.game.helper.net.model;

public class RegistRequestBody extends BaseRequestBody {
    public String phone;
    public String code;
    public String market_num;

    public RegistRequestBody(String phone, String code, String market_num) {
        super(1);
        this.phone = phone;
        this.code = code;
        this.market_num = market_num;
    }
}
