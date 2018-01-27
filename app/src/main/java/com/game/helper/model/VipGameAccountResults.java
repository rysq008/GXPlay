package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

public class VipGameAccountResults extends XBaseModel {
    /*
   {
    "message": "成功",
    "code": "0000",
    "data": {
        "count": 2,
        "is_highest_vip": false,
        "vip_level": 1
    }
}
    */
    public int count;
    public int vip_level;
    public boolean is_highest_vip;//是不是最高等级vip
}
