package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class ForgetPasswdResults extends XBaseModel {

    @Override
    public int itemType() {
        return RxConstant.AccountModeType.Account_ResetPasswd_type;
    }
}
