package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

public class H5Results extends XBaseModel {

          /*"market_url": "http://60.205.204.218:8080/G9/promotion_sy.html",
          "vip_url": "http://60.205.204.218:8080/G9/vip.html",
          "account_guide_url": "http://60.205.204.218:8080/G9/acount_guide.html",
          "expected_url": "http://60.205.204.218:8080/G9/forward.html",
          "share_discount_url": "http://www.baidu.com"*/
    public String market_url;
    public String vip_url;
    public String account_guide_url;


    @Override
    public int itemType() {
        return RxConstant.HomeModeType.H5_Model_Type;
    }
}
