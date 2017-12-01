package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class CommonResults extends XBaseModel {

    @SerializedName("list")
    public List<CommonItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }


    public static class CommonItem implements ItemType {
        //"message": "成功",
//        "code": "0000",
//        "data": {
//            "list": [
//            {
//                "id": 1,
//                    "name": "单机"
//            },
//            {
//                "id": 2,
//                    "name": "网游"
//            },
//            {
//                "id": 3,
//                    "name": "手机页游"
//            },
//            {
//                "id": 4,
//                    "name": "变态手游"
//            }
//        ]
        public int id;
        public String name;

        @Override
        public int itemType() {
            return RxConstant.GameModeType.Game_Common_type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.GameModeType.Game_Common_type;
    }
}
