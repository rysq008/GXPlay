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
//        } {"message": "\u6210\u529f", "code": "0000", "data": {"has_trade_passwd": false, "phone": "18610488286", "has_passwd": true, "has_alipay_account": false, "member_id": 38}}
    public String phone;
    public String member_id;
    public boolean has_passwd = false;
    public boolean has_trade_passwd = false;
    public boolean has_alipay_account = false;

    @Override
    public int itemType() {
        return RxConstant.Login_Type;
    }
}
