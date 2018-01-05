package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class VersionInfoResults extends XBaseModel {
/*
 {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "url": "http://ghelper.h5h5h5.cn/upload/apk/G9Game.apk",
                "version": "2.0.6",
                "create_time": "2017-12-13 16:06:20",
                "id": 2,
                "description": "1.更新啦 2.很好玩"
            },
            {
                "url": "http://ghelper.h5h5h5.cn/upload/apk/G9Game.apk",
                "version": "2.0.5",
                "create_time": "2017-12-13 15:52:29",
                "id": 1,
                "description": "123123"
            }
        ]
    }
}
 * */
    @SerializedName("list")
    public List<VersionInfoItem> list;

    public class VersionInfoItem implements ItemType {
        public String url;
        public String version;
        public String create_time;
        public int id;
        public String description;

        @Override
        public int itemType() {
            return 0;
        }
    }

    @Override
    public int itemType() {
            return RxConstant.WalletModelType.Wallet_Cash_To_Type;
        }
}
