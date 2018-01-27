package com.game.helper.net.model;

public class AddGameAccountRequestBody extends BaseRequestBody {

    private int game_id;
    private int plat_id;
    private int channel_id;
    private String game_account;
    private boolean bind_vip;

    public AddGameAccountRequestBody(int game_id, int plat_id, int channel_id, String game_account, boolean bvip) {
        super(1);
        this.game_id = game_id;
        this.plat_id = plat_id;
        this.channel_id = channel_id;
        this.game_account = game_account;
        bind_vip = bvip;
    }

}
