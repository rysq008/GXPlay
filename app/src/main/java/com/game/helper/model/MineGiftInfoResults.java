package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * */
public class MineGiftInfoResults extends XBaseModel {
    /*
{
    "message": "成功",
    "code": "0000",
    "data": {
        "use_method": "",
        "remain_num": 1,
        "start_time": "2017-11-01",
        "game": {
            "logo": "/upload/images/gameinfo/2017110117343428767.png",
            "logothumb": "",
            "id": 1,
        },
        "end_time": "2017-11-16",
        "gift_content": "123123123",
        "id": 1,
        "giftname": "123123"
    }
}
    * */

    public String use_method;
    public String remain_num;
    public String start_time;
    public Game game;
    public String end_time;
    public String gift_content;
    public int id;
    public String giftname;

    public class Game implements ItemType {
        public String logo;
        public String logothumb;
        public int id;
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
