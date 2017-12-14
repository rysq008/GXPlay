package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class CashListResults extends XBaseModel {
    /*
    {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "finish_time": "2017-11-15 16:01:20",
                "amount": "50.00",
                "apliy_account": "123456789"
            },
            {
                "finish_time": "2017-11-15 15:51:00",
                "amount": "50.00",
                "apliy_account": "123456789"
            },
            {
                "finish_time": "2017-11-14 18:49:05",
                "amount": "20.00",
                "apliy_account": "123456789"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}

    */

    @SerializedName("list")
    public List<CashListItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class CashListItem implements ItemType {
        /*{
            {
                "finish_time": "2017-11-14 18:49:05",
                "amount": "20.00",
                "apliy_account": "123456789"
            }
        */
        public String finish_time;
        public String amount;
        public String apliy_account;

        @Override
        public int itemType() {
            return RxConstant.WalletModelType.Wallet_Cash_type;
        }
    }

    @Override
    public int itemType() {
            return RxConstant.WalletModelType.Wallet_Cash_type;
        }
}
