package com.game.helper.net.model;

public class DeleteGameRequestBody extends BaseRequestBody {
    public int my_package_id;

    public DeleteGameRequestBody(int my_package_id) {
        super(1);
        this.my_package_id = my_package_id;
    }

}
