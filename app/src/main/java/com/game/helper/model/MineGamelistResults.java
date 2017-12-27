package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * */
public class MineGamelistResults extends XBaseModel {
    /*
   {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "name_package": "zgyx.apk",
                "game": {
                    "logo": "/upload/images/gameinfo/2017110916250155668.png",
                    "id": 3,
                    "logothumb": "",
                    "name": "梦幻手游"
                },
                "id": 6,
                "channel": {
                    "id": 1,
                    "name": "果盘"
                }
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    * */
    @SerializedName("list")
    public List<MineGamelistItem> list;

    public static class MineGamelistItem implements ItemType{
        public int game_package_id;
        public String game_package_filesize;
        public Game game;
        public int id;
        public Channel channel;

        public class Game implements ItemType {
            public String logo;
            public String type;
            public int id;
            public String name;
            @Override
            public int itemType() {
                return 0;
            }
        }

        public class Channel implements ItemType {
            public int id;
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
