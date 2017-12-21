package com.game.helper.net.model;

public class SingleGameIdRequestBody extends BaseRequestBody {

    public int game_account_id;

    public SingleGameIdRequestBody(int game_account_id) {
        super(1);
        this.game_account_id = game_account_id;
    }

}
