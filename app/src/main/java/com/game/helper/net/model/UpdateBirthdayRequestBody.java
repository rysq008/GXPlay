package com.game.helper.net.model;

public class UpdateBirthdayRequestBody extends BaseRequestBody {
    public String birthday;//生日 格式例子 “1990-02-03”

    public UpdateBirthdayRequestBody(String birthday) {
        super(1);
        this.birthday = birthday;
    }
}
