package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * */
public class MineOrderlistResults extends XBaseModel {
    /*
 {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "status": 2,
                "game_account": "15201675725",
                "game_logo": "/upload/image/20161209/20161209195320_406.jpg",
                "amount": "100.00",
                "create_time": "2017-12-07 11:39:52",
                "game_name": "大话西游"
            },
            {
                "status": 2,
                "game_account": "15201675725",
                "game_logo": "/upload/image/20161209/20161209195320_406.jpg",
                "amount": "100.00",
                "create_time": "2017-12-07 11:21:50",
                "game_name": "大话西游"
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<MineOrderlistItem> list;

    public static class MineOrderlistItem implements ItemType{
        /*
        * */
        public int status;//1为未受理 2为已受理 3已退单
        public String game_account;
        public String game_logo;
        public String amount;
        public String create_time;
        public String game_name;

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
