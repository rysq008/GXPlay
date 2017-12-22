package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class UnAvailableRedpackResultModel extends XBaseModel {
/*
    {
        "message": "成功",
            "code": "0000",
            "data": {
        "list": [
        {
            "status": "已过期",
                "red_id": 4,
                "amount": "13.14",
                "money_limit": "50.00",
                "games": [
            {
                "name": "航海王强者之路"
            },
            {
                "name": "放开那三国2（乐游旧版）"
            },
            {
                "name": "梦幻西游"
            }
                ],
            "name": "七夕红包",
                "end_date": "2017-12-02",
                "my_red_id": 4,
                "type": 2,
                "kind": 1
        },
        {
            "status": "已使用",
                "red_id": 1,
                "amount": "10.00",
                "money_limit": "50.00",
                "games": [],
            "name": "注册红包",
                "end_date": "2017-12-17",
                "my_red_id": 0,
                "type": 1,
                "kind": 2
        }
        ]
    },
        "total_page": 1,
            "current_page": 1
    }
    */
    @SerializedName("list")
    public List<UnAvailableRedpackItem> list;

    public class UnAvailableRedpackItem implements ItemType {
        public String status;
        public int red_id;
        public String amount;
        public String money_limit;
        @SerializedName("games")
        public List<Game> games;
        public String name;
        public String end_date;
        public int my_red_id;
        public int type;
        public int kind;

        public class Game implements ItemType {
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

    @Override
    public int itemType() {
        return 0;
    }
}
