package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class MarketInfoResults extends XBaseModel {
/*
{
    "message": "成功",
    "code": "0000",
    "data": {
        "yue_history": "0.00",
        "yujizongshouyi": "200.00",
        "url": "http://127.0.0.1:8000/?market_id=2&market_num=10000&signature=081425df59351e65f96ea0ee1f6fe8b4",
        "viprenshu": 0,
        "zongshouyi": "0.00",
        "member": {
            "nick_name": "这个昵称很好",
            "phone": "13312341234",
            "id": 1,
            "icon": ""
        },
        "total_promotion_number": 4,
        "num": "10000",
        "dongjie": "0.00",
        "yue": "0.00",
        "id": 2
    }
}
}
* */
   public String yue_history;
   public String yujizongshouyi;
   public String url;
   public int viprenshu;
   public String zongshouyi;
   public Member member;
   public int total_promotion_number;
   public String num;
   public String dongjie;
   public String yue;
   public int id;

   public static class Member implements ItemType{
        /*
        *  "member": {
                "nick_name": "这个昵称很好",
                "phone": "13312341234",
                "id": 1,
                "icon": ""
            },
        * */
        public String nick_name;
        public String phone;
        public int id;
        public String icon;

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
