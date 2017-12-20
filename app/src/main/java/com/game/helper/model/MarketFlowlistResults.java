package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class MarketFlowlistResults extends XBaseModel {
/*
{
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "recommended": {
                    "nick_name": "826277"
                },
                "create_time": "2017-11-10 15:45:53",
                "type": "充值VIP返利",
                "reward": "100.00"
            },
            {
                "recommended": {
                    "nick_name": "帅气的昵称"
                },
                "create_time": "2017-10-29 15:56:00",
                "type": "普通充值返利",
                "reward": "20.00"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
* */
    @SerializedName("list")
    public List<MarketFlowlistItem> list;

    public static class MarketFlowlistItem implements ItemType{
        /*
          {
                "recommended": {
                    "nick_name": "帅气的昵称"
                },
                "create_time": "2017-10-29 15:56:00",
                "type": "普通充值返利",
                "reward": "20.00"
            }
        * */
        public Recommended recommended;
        public String create_time;
        public String type;
        public String reward;

        public static class Recommended implements ItemType {

            public String nick_name;
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
    @Override
    public int itemType() {
            return 0;
        }
}
