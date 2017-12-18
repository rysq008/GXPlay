package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

public class GameAccountDiscountResults extends XBaseModel {
    /*
  {
    "message": "成功",
    "code": "0000",
    "data": {
        "vip_discount": 0,
        "high_vip_discount": 0,
        "member_discount": 0
    }
}
    */
    public float vip_discount;//vip会员折扣
    public float high_vip_discount;//黄冠折扣(首冲可体验一次)
    public float member_discount;//普通会员折扣

    @Override
    public int itemType() {
            return 0;
        }
}
