package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class HotResults extends XBaseModel {

    @SerializedName("list")
    public List<HotItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }


    public static class HotItem extends XBaseModel {
        /* {
            "game_package": {
            "zhekou_shouchong": 4.2,
                    "discount_activity": 3.5,
                    "filesize": 123
        },
            "name": "少年西游记",
                "logothumb": "/media/game_logo/f0cf3e7ec227f67918d94237aecd7959.jpg",
                "intro": "很好玩的啊",
                "logo": "/upload/images/gameinfo/2017111617074546201.png",
                "type": {
            "id": 1,
                    "name": "策略回合"
        },
            "class_type": {
            "id": 1,
                    "name": "网游"
        },
            "id": 4
        }*/
        public int id;
        public Map<String, Float> game_package;//
        public String name;
        public String logothumb;
        public String intro;
        public String logo;
        public Map<String, String> type;
        /*"class_type": {
           "id": 1,
                   "name": "网游"
       }*/
        public Map<String, String> class_type;

        @Override
        public int itemType() {
            return RxConstant.HomeModeType.Hot_Model_Type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.Hot_Model_Type;
    }
}
