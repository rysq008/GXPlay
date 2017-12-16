package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class RegistResults extends XBaseModel {

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
    public boolean has_passwd = true;
    public boolean has_trade_passwd = false;
    public boolean has_alipay_account = false;

    @Override
    public int itemType() {
            return RxConstant.AccountModeType.Account_Regist_type;
        }
}
