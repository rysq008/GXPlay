package com.game.helper.net.model;

public class GameListRequestBody extends BaseRequestBody {
    private String word;

    public GameListRequestBody(int page,String word) {
        super(page);
        this.word = word;
    }

}
