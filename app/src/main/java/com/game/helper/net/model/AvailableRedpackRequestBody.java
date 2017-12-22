package com.game.helper.net.model;

public class AvailableRedpackRequestBody extends BaseRequestBody {
    private int option_game_id;
    private String money;

    public AvailableRedpackRequestBody(int page,int option_game_id,String money) {
        super(page);
        this.option_game_id = option_game_id;
        this.money = money;
    }

}
