package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class RecommendResults extends XBaseModel {

    @SerializedName("list")
    public List<RecommendItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }


    public static class RecommendItem extends XBaseModel {
        //        {
//            "game_package": {
//         * "zhekou_shouchong": 4.2,
//                 * "discount_activity": 3.5,
//                 * "filesize": 211,
//        },
//            "name": "少年西游记",
//                "logothumb": "/media/game_logo/f0cf3e7ec227f67918d94237aecd7959.jpg",
//                "intro": "很好玩的啊",
//                "logo": "/media/game_logo/4633079c6096782f654f9fa8cb0d9497.jpg",
//                "type": {
//            "id": 1,
//                    "name": "策略回合"
//        },
//            "id": 4
//        }
        public int id;
        //            "zhekou_shouchong": 4.3,
        //                    "filesize": 211
        public Map<String, Float> game_package;//
        public String name;
        public String logothumb;
        public String intro;
        public String logo;
        //            "id": 1,
//                    "name": "策略回合"
        public Map<String, String> type;

        @Override
        public int itemType() {
            return RxConstant.HomeModeType.Recommend_Model_Type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.Recommend_Model_Type;
    }
}
