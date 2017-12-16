package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class RechargeListResults extends XBaseModel {
    /*{
        "message": "成功",
        "code": "0000",
        "data": {
            "list": [
                {
                    "finish_time": "2017-11-21 09:52:22",
                    "jine": "1000.00"
                },
                {
                    "finish_time": "2017-11-16 11:18:17",
                    "jine": "1000.00"
                },
                {
                    "finish_time": "2017-11-16 11:18:17",
                    "jine": "1000.00"
                }
            ]
        },
        "total_page": 1,
        "current_page": 1
    }
    }*/

    @SerializedName("list")
    public List<RechargeListItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class RechargeListItem implements ItemType {
        /*{
            {
                    "finish_time": "2017-11-16 11:18:17",
                    "jine": "1000.00"
                }
        */
        public String finish_time;
        public String jine;

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
