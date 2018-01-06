package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * */
public class SystemMessageResults extends XBaseModel {
    /*
  {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "is_read": 1,
                "title": "提现成功",
                "content": "尊敬的用户您好:订单号:GRE2017112111100148032的订单已提现成功。祝您游戏愉快，心想事成。遇到任何问题请联系客服处理，为您的游戏体验保驾护航。",
                "create_time": "2017-11-21 11:15:33",
                "type": "1",
                "id": 34
            },
            {
                "is_read": 1,
                "title": "群3",
                "content": "单位各 爱我的 ",
                "create_time": "2017-11-14 14:08:57",
                "type": "2",
                "id": 3
            },
            {
                "is_read": 1,
                "title": "群2",
                "content": "asdagwafg",
                "create_time": "2017-11-14 11:00:17",
                "type": "2",
                "id": 2
            },
            {
                "is_read": 1,
                "title": "群1",
                "content": "11234",
                "create_time": "2017-11-13 19:14:54",
                "type": "2",
                "id": 1
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<SystemMessageItem> list;

    public static class SystemMessageItem extends XBaseModel {
        public int is_read;
        public int id;
        public String title;
        public String content;
        public String create_time;
        public String type;
    }
}
