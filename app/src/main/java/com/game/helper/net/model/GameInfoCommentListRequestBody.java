package com.game.helper.net.model;

public class GameInfoCommentListRequestBody extends BaseRequestBody {
    private int game_id;

    public GameInfoCommentListRequestBody(int page, int game_id) {
        super(page);
        this.game_id = game_id;
    }

}
