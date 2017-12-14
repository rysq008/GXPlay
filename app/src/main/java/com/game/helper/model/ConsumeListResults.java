package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import zlc.season.practicalrecyclerview.ItemType;

public class ConsumeListResults extends XBaseModel {
    /*{
        "message": "成功",
            "code": "0000",
            "data": {
        "list": [
        {
            "name": "梦幻西游",
                "img": "/upload/image/20161209/20161209135549_943.jpg",
                "finish_time": "2017-12-08 14:17:00",
                "game_account": "13955555555",
                "channel_name": "乐嗨嗨",
                "amount": "10.00",
                "type": 2
        },
        {
            "name": "黄钻",
                "img": "/upload/images/sys/2017112710530310113.png",
                "finish_time": "2017-11-21 09:56:28",
                "game_account": "",
                "channel_name": "",
                "amount": "299.00",
                "type": 1
        },
        {
            "name": "黄钻",
                "img": "/upload/images/sys/2017112710530310113.png",
                "finish_time": "2017-11-16 11:40:47",
                "game_account": "",
                "channel_name": "",
                "amount": "299.00",
                "type": 1
        }
        ]
    },
        "total_page": 1,
            "current_page": 1
    }*/
    @SerializedName("list")
    public List<ConsumeListItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class ConsumeListItem implements ItemType{
        /*{
            "name": "黄钻",
                "img": "/upload/images/sys/2017112710530310113.png",
                "finish_time": "2017-11-16 11:40:47",
                "game_account": "",
                "channel_name": "",
                "amount": "299.00",
                "type": 1
        }*/
        public String name;
        public String img;
        public String finish_time;
        public String game_account;
        public String channel_name;
        public String amount;
        public int type;

        @Override
        public int itemType() {
            return RxConstant.WalletModelType.Wallet_Consume_type;
        }
    }

    @Override
    public int itemType() {
            return RxConstant.WalletModelType.Wallet_Consume_type;
        }
}
