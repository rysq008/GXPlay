package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class ProfitListResults extends XBaseModel {
    /*

    {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "type": 1,
                "recommended": {
                    "nick_name": "826277"
                },
                "reward": "100.00",
                "create_time": "2017-11-10 15:45:53"
            },
            {
                "type": 2,
                "recommended": {
                    "nick_name": "帅气的昵称"
                },
                "reward": "20.00",
                "create_time": "2017-10-29 15:56:00"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    */

    @SerializedName("list")
    public List<ProfitListItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class ProfitListItem implements ItemType {
        /*{
           {
                "type": 1,
                "recommended": {
                    "nick_name": "826277"
                },
                "reward": "100.00",
                "create_time": "2017-11-10 15:45:53"
            },
        */
        public int type;
        public Recommended recommended;
        public String finish_time;
        public String reward;
        public String create_time;

        public static class Recommended implements ItemType{

            public String nick_name;
            @Override
            public int itemType() {
                return RxConstant.WalletModelType.Wallet_Recharge_type;
            }
        }

        @Override
        public int itemType() {
            return RxConstant.WalletModelType.Wallet_Recharge_type;
        }
    }

    @Override
    public int itemType() {
            return RxConstant.WalletModelType.Wallet_Recharge_type;
        }
}
