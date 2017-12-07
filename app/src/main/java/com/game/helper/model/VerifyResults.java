package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class VerifyResults extends XBaseModel {

//    {
//        "code": '0000',
//            "msg":"发送成功",
//            "data": {}
//    }

    @Override
    public int itemType() {
        return RxConstant.AccountModeType.Account_Vertify_type;
    }
}
