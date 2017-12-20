package com.game.helper.net.model;

public class MineGameRequestBody extends BaseRequestBody {
    public int plat_id;

    public MineGameRequestBody(int page,int plat_id) {
        super(page);
        this.plat_id = plat_id;
    }

}
