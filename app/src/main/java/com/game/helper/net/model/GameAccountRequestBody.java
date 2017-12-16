package com.game.helper.net.model;

public class GameAccountRequestBody extends BaseRequestBody {
    public int plat_id;
    public int option_game_id;
    public int option_channel_id;

    public GameAccountRequestBody(int page,int plat_id,int option_game_id, int option_channel_id) {
        super(page);
        this.plat_id = plat_id;
        this.option_game_id = option_game_id;
        this.option_channel_id = option_channel_id;
    }
}
