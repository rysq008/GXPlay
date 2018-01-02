package com.game.helper.net.model;

public class FeedbakcStatusRequestBody extends BaseRequestBody {
    public int status;
    public int feedback_id;

    public FeedbakcStatusRequestBody(int status, int feedback_id) {
        super(1);
        this.status = status;
        this.feedback_id = feedback_id;
    }

}
