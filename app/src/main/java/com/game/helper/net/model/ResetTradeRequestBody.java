package com.game.helper.net.model;

public class ResetTradeRequestBody extends BaseRequestBody {

    public String passwd;
    public String code;
    public String real_name;

    public ResetTradeRequestBody( String passwd, String code) {
        super(1);
        this.passwd = passwd;
        this.code = code;
    }

    public String idno_six;

    public ResetTradeRequestBody(String passwd, String code, String real_name, String idno_six) {
        super(1);
        this.passwd = passwd;
        this.code = code;
        this.real_name = real_name;
        this.idno_six = idno_six;
    }
}
