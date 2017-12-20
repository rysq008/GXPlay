package com.game.helper.net.model;

public class SetTradeRequestBody extends BaseRequestBody {

    public String passwd;
    public String real_name;
    public String idno_six;

    public SetTradeRequestBody(String passwd, String real_name, String idno_six) {
        super(1);
        this.passwd = passwd;
        this.real_name = real_name;
        this.idno_six = idno_six;
    }
}
