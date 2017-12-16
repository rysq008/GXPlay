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
        public boolean status;
        public String url;
        public String image;
        public String name;
        public String create_time;
        public int id;
        public int game_package_id;//: 0,
        public int type;//banner 类型(1为H5界面，2为游戏): 1,

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
