package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * 不关心结果的result／只需要判断code = 0000
 * */
public class MarketExpectedFlowlistResults extends XBaseModel {
    /*
   {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "status": "待激活",
                "amount": "200",
                "name": "这个昵称很好"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<MarketExpectedFlowlistItem> list;

    public static class MarketExpectedFlowlistItem implements ItemType{
        /*
         {
                "status": "待激活",
                "amount": "200",
                "name": "这个昵称很好"
            }
        * */
        public String status;
        public String amount;
        public String name;

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
