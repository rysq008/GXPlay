package com.game.helper.net.model;

public class UpdateSignatrueRequestBody extends BaseRequestBody {

    public String signature;

    public UpdateSignatrueRequestBody(String signature) {
        super(1);
        this.signature = signature;
    }
}
