package com.game.helper.net.model;

public class DeleteGiftRequestBody extends BaseRequestBody {
    public int my_gift_code_id;

    public DeleteGiftRequestBody(int my_gift_code_id) {
        super(1);
        this.my_gift_code_id = my_gift_code_id;
    }

}
