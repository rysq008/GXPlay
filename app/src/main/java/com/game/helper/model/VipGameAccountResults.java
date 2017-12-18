package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class VipGameAccountResults extends XBaseModel {
    /*
   {
    "message": "成功",
    "code": "0000",
    "data": {
        "count": 1,
        "vip_level": 1
    }
}
    */
    public int count;
    public int vip_level;
    public boolean is_highest_vip;//是不是最高等级vip

    @Override
    public int itemType() {
            return 0;
        }
}
