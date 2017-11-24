package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class SpecialResults extends XBaseModel {

    @SerializedName("list")
    public List<SpecialItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class SpecialItem implements ItemType {
        //        {
        //            "content": "好玩的三国",
        //                "image": "/upload/images/gameinfo/2017110610481357228.png",
        //                "id": 1,
        //                "name": "热血三国"
        //        },
        public String content;
        public String image;
        public int id;
        public String name;

        @Override
        public int itemType() {
            return RxConstant.HomeModeType.Special_Model_Type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.Special_Model_Type;
    }
}
