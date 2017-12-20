package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * */
public class MineGiftlistResults extends XBaseModel {
    /*
  {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "id": 2,
                "gift_code": {
                    "code": "23456677",
                    "id": 2,
                    "gift": {
                        "game": {
                            "logo": "/upload/images/gameinfo/2017110117343428767.png",
                            "id": 1,
                            "logothumb": ""
                        },
                        "end_time": "2017-12-16",
                        "start_time": "2017-11-01",
                        "id": 1,
                        "giftname": "123123"
                    }
                }
            },
            {
                "id": 3,
                "gift_code": {
                    "code": "34567123",
                    "id": 3,
                    "gift": {
                        "game": {
                            "logo": "/upload/images/gameinfo/2017110117343428767.png",
                            "id": 1,
                            "logothumb": ""
                        },
                        "end_time": "2017-12-16",
                        "start_time": "2017-11-01",
                        "id": 1,
                        "giftname": "123123"
                    }
                }
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<MineGiftlistItem> list;

    public static class MineGiftlistItem implements ItemType{
        /*
     {
                "id": 3,
                "gift_code": {
                    "code": "34567123",
                    "id": 3,
                    "gift": {
                        "game": {
                            "logo": "/upload/images/gameinfo/2017110117343428767.png",
                            "id": 1,
                            "logothumb": ""
                        },
                        "end_time": "2017-12-16",
                        "start_time": "2017-11-01",
                        "id": 1,
                        "giftname": "123123"
                    }
                }
            }
        * */
        public int id;
        public GiftCode gift_code;

        public class GiftCode implements ItemType {
            public String code;
            public int id;
            public Gift gift;

            public class Gift implements ItemType {
                public Game game;
                public String end_time;
                public String start_time;
                public int id;
                public String giftname;

                public class Game implements ItemType {
                    public String logo;
                    public int id;
                    public String logothumb;
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
