package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import zlc.season.practicalrecyclerview.ItemType;

public class HotResults extends XBaseModel {

    @SerializedName("list")
    public List<HotItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }


    public static class HotItem implements ItemType {
        /**
         * "game_package": {
         * "zhekou_shouchong": 4.3,
         * "zhekou_xuchong": 5.5,
         * "filesize": 211,
         * "discount_vip": 0
         * },
         * "name": "少年西游记",
         * "logothumb": "/media/game_logo/f0cf3e7ec227f67918d94237aecd7959.jpg",
         * "intro": "很好玩的啊",
         * "logo": "/upload/images/gameinfo/2017111617074546201.png",
         * "type": {
         * "id": 1,
         * "name": "策略回合"
         * },
         * "id": 4
         */
        public int id;
        public Map<String, Float> game_package;//
        public String name;
        public String logothumb;
        public String intro;
        public String logo;
        public Map<String, String> type;

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
