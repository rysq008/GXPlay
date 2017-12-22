package com.game.helper.net.model;

public class ForgetPasswdRequestBody extends BaseRequestBody {

    public String phone;
    public String passwd;
    public String code;

    public ForgetPasswdRequestBody(String phone, String passwd, String code) {
        super(1);
        this.phone = phone;
        this.passwd = passwd;
        this.code = code;
    }
}
