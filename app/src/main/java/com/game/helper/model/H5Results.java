package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class H5Results extends XBaseModel {

    //   "market_url": "http://60.205.204.218:8080/G9/promotion_sy.html",
//           "vip_url": "http://60.205.204.218:8080/G9/vip.html"
    public String market_url;
    public String vip_url;

    @Override
    public int itemType() {
        return RxConstant.HomeModeType.H5_Model_Type;
    }
}
