package com.game.helper.net.model;

public class MineGiftInfoRequestBody extends BaseRequestBody {
    public int gift_id;

    public MineGiftInfoRequestBody(int gift_id) {
        super(1);
        this.gift_id = gift_id;
    }

}
