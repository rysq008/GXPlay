package com.game.helper.net.model;

public class AvailableRedpackRequestBody extends BaseRequestBody {
    private int option_game_id;

    public AvailableRedpackRequestBody(int page,int option_game_id) {
        super(page);
        this.option_game_id = option_game_id;
    }

}
