package com.game.helper.net.model;

public class UpdateNicknameRequestBody extends BaseRequestBody {
    public String nickname;

    public UpdateNicknameRequestBody(String nickname) {
        super(1);
        this.nickname = nickname;
    }
}
