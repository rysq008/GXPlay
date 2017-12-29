package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * */
public class PlatformMessageResults extends XBaseModel {
    /*
  {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "discount": 5,
                "amount": "100.00",
                "consume_amount": "50.00",
                "create_time": "2017-12-07 11:39:52"
            },
            {
                "discount": 5,
                "amount": "100.00",
                "consume_amount": "50.00",
                "create_time": "2017-12-07 11:21:50"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<MineGameDesclistItem> list;

    public static class MineGameDesclistItem implements ItemType{
        public float discount;
        public String amount;
        public String consume_amount;
        public String create_time;

        @Override
        public int itemType() {
            return 0;
        }
    }
    @Override
    public int itemType() {
            return 0;
        }
}
