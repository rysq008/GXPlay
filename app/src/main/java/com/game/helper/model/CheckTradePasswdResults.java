package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class CheckTradePasswdResults extends XBaseModel {
    /*
{
    "message": "成功",
    "code": "0000",
    "data": {}
}

    */


    @Override
    public int itemType() {
            return RxConstant.Proving_Trade_Password;
        }
}