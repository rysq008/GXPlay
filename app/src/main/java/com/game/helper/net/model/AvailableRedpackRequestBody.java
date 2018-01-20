package com.game.helper.net.model;

public class AvailableRedpackRequestBody extends BaseRequestBody {
    private int  game_account_id;
    private String money;

    public AvailableRedpackRequestBody(int page,int  game_account_id,String money) {
        super(page);
        this. game_account_id =  game_account_id;
        this.money = money;
    }

}
