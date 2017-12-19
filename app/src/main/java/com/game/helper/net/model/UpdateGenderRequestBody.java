package com.game.helper.net.model;

public class UpdateGenderRequestBody extends BaseRequestBody {
    public String gender;//用户性别，”0”:女；”1”:男；”2”:’-‘

    public UpdateGenderRequestBody(String gender) {
        super(1);
        this.gender = gender;
    }
}
