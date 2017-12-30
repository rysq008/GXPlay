package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

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
                "content": "送送送送送送送送送送送送送送送送送送送送送送送送送送送送送送",
                "create_time": "2017-12-27 14:31:58",
                "id": 2,
                "title": "新年送好礼"
            },
            {
                "content": "送送送",
                "create_time": "2017-12-27 13:55:53",
                "id": 1,
                "title": "元旦送好礼"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<SystemMessageItem> list;

    public static class SystemMessageItem implements ItemType{
        public String content;
        public int id;
        public String title;
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
