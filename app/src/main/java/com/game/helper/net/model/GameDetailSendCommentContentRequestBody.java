package com.game.helper.net.model;

public class GameDetailSendCommentContentRequestBody  {
    public int game_id;
    public String content;

    public GameDetailSendCommentContentRequestBody(int game_id,String content) {
        this.game_id = game_id;
        this.content = content;
    }

}
