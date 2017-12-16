package com.game.helper.net.model;

public class RegistRequestBody extends BaseRequestBody {
    public String phone;
    public String passwd;
    public String code;
    public String market_num;

    public RegistRequestBody(String phone,String passwd, String code, String market_num) {
        super(1);
        this.phone = phone;
        this.passwd = passwd;
        this.code = code;
        this.market_num = market_num;
    }
}
