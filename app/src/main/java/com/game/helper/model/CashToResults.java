package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class CashToResults extends XBaseModel {
    /*
   {"message":"推广账户可用余额不足","data":null,"code":"0106"}
    */


    @Override
    public int itemType() {
            return RxConstant.WalletModelType.Wallet_Cash_To_Type;
        }
}
