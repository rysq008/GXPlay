package com.game.helper.net.model;

public class VerifyRequestBody extends BaseRequestBody {
    public String phone;
    public String usefor;

    public VerifyRequestBody(String phone, String usefor) {
        super(1);
        this.phone = phone;
        this.usefor = usefor;
    }
}
