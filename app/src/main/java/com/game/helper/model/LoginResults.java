package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class LoginResults extends XBaseModel {

    public String phone;
    public int member_id;

    @Override
    public int itemType() {
        return RxConstant.Login_Type;
    }
}
