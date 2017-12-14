package com.game.helper.net.model;

public class BannerRequestBody extends BaseRequestBody {

    public int position_type;

    public BannerRequestBody(int position_type) {
        super(1);
        this.position_type = position_type;
    }
}
