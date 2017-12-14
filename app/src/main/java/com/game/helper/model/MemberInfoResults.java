package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zlc.season.practicalrecyclerview.ItemType;

public class MemberInfoResults extends XBaseModel {
//        {
//            "message": "成功",
//                "code": "0000",
//                "data": {
//            "icon_thumb": "",
//                    "nick_name": "这个昵称很好",
//                    "gender": "2",
//                    "phone": "13312341234",
//                    "vip_level": {
//                "image": "",
//                        "level": 1,
//                        "name": "",
//                        "descs": "享受vip充值折扣待遇"
//            },
//            "birthday": "1991-03-02",
//                    "signature": "",
//                    "address_count": 0,
//                    "balance": "920.00",
//                    "icon": ""
//        }
//        }
    public String icon_thumb;
    public String nick_name;
    public String gender;
    public String phone;
//                        "image": "",
//                        "level": 1,
//                        "name": "",
//                        "descs": "享受vip充值折扣待遇"
    public Map<String,String> vip_level;
    public String birthday;
    public String signature;
    public int address_count;
    public String balance;
    public String market_balance;
    public String total_balance;
    public String icon;

    @Override
    public int itemType() {
            return RxConstant.AccountModeType.Account_Member_Info_type;
        }
}
