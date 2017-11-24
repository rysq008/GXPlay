package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class NoticeResults extends XBaseModel {

    @SerializedName("list")
    public List<NoticeItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }


    public static class NoticeItem implements ItemType {

        //        {
//            "content": "微信支付修理好了",
//                "id": 1
//        }
        public String content;
        public int id;

        @Override
        public int itemType() {
            return RxConstant.HomeModeType.Notice_Model_Type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.Notice_Model_Type;
    }
}
