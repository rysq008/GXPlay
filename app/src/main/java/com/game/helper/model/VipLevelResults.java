package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class VipLevelResults extends XBaseModel {
    /*
  {
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "name": "黄钻",
                "year_fee": "",
                "level": 0,
                "image": "",
                "descs": "",
                "num_account": "",
                "no_re_rate": "0.20",
                "vip_re_amount": "",
                "id": 99
            },
            {
                "name": "黑钻",
                "year_fee": 298,
                "level": 1,
                "image": "/upload/images/sys/2017112710530310113.png",
                "descs": "享受vip充值折扣待遇",
                "num_account": 2,
                "no_re_rate": "0.30",
                "vip_re_amount": "200.00",
                "id": 1
            },
            {
                "name": "红钻",
                "year_fee": 599,
                "level": 2,
                "image": "/upload/images/sys/2017111710353353889.png",
                "descs": "1",
                "num_account": 3,
                "no_re_rate": "0.40",
                "vip_re_amount": "300.00",
                "id": 2
            },
            {
                "name": "皇冠",
                "year_fee": 899,
                "level": 3,
                "image": "/upload/images/sys/2017111710354315372.png",
                "descs": "1",
                "num_account": 4,
                "no_re_rate": "0.50",
                "vip_re_amount": "500.00",
                "id": 3
            }
        ]
    }
}
    */

    @SerializedName("list")
    public List<VipBean> list;

    public static class VipBean implements ItemType {
        /*{
       {
                "name": "黄钻",
                "year_fee": "",
                "level": 0,
                "image": "",
                "descs": "",
                "num_account": "",
                "no_re_rate": "0.20",
                "vip_re_amount": "",
                "id": 99
            },
        }*/

        public String name;
        public String year_fee;
        public int level;
        public String image;
        public String descs;
        public String num_account;
        public String no_re_rate;//奖励比例
        public String vip_re_amount;//奖励金额
        public int id;

        @Override
        public int itemType() {
            return 0;
        }
    }


    @Override
    public int itemType() {
            return 0;
        }
}
