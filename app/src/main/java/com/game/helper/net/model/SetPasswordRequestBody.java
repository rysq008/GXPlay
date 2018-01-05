package com.game.helper.net.model;

public class SetPasswordRequestBody extends BaseRequestBody {

    public String passwd;

    public SetPasswordRequestBody(String passwd) {
        super(1);
        this.passwd = passwd;
    }
}
