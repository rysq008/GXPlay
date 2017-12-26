package com.game.helper.net.model;

public class GameInfoGiftListRequestBody extends BaseRequestBody {
    private int game_id;
    private int plat_id;

    public GameInfoGiftListRequestBody(int page, int game_id, int plat_id) {
        super(page);
        this.game_id = game_id;
        this.plat_id = plat_id;
    }

}
