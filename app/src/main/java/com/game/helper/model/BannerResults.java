package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class BannerResults extends XBaseModel {

    @SerializedName("list")
    public List<BannerItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class BannerItem implements ItemType {
        //            "status":true,
        //                    "name":"广告",
        //                    "url":"https://www.showdoc.cc/1667248?page_id=15559414",
        //                    "image":"/upload/images/banner/2017111411253941125.png",
        //                    "create_time":"2017-11-13 15:55:56",
        //                    "id":1
        public boolean status;
        public String url;
        public String image;
        public String name;
        public String create_time;
        public int id;

        @Override
        public int itemType() {
            return RxConstant.HomeModeType.Banner_Model_Type;
        }
    }

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.Banner_Model_Type;
    }
}
