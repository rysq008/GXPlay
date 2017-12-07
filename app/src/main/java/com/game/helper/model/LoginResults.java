package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class LoginResults extends XBaseModel {

//        {
//            "message": "成功",
//                "code": "0000",
//                "data": {
//            "phone": "18511249037",
//                    "member_id": 15
//        }
//        }
    public String phone;
    public String member_id;

    @Override
    public int itemType() {
        return RxConstant.AccountModeType.Account_Login_type;
    }
}
