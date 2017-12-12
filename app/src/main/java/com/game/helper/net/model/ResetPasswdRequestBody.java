package com.game.helper.net.model;

public class ResetPasswdRequestBody extends BaseRequestBody {

    public String passwd;
    public String code;

    public ResetPasswdRequestBody(String passwd, String code) {
        super(1);
        this.passwd = passwd;
        this.code = code;
    }
}
