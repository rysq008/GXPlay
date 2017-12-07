package com.game.helper.net.model;

public class LoginRequestBody extends BaseRequestBody {
    public String phone;
    public String code;
    public String type;
    public String channel_num;

    public LoginRequestBody(String phone, String code, String type, String channel_num) {
        super(1);
        this.phone = phone;
        this.code = code;
        this.type = type;
        this.channel_num = channel_num;
    }
}
