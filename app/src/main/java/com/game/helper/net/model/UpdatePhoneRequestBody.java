package com.game.helper.net.model;

public class UpdatePhoneRequestBody extends BaseRequestBody {
    public String phone;
    public String code;

    public UpdatePhoneRequestBody(String phone, String code) {
        super(1);
        this.phone = phone;
        this.code = code;
    }
}
