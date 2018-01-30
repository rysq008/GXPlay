package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2017/12/28.
 */

public class H5UrlListResults extends XBaseModel {

//            "market_url": "https://apip.g9yx.com/h5/promotion_sy.html",
//                    "activity_url": "https://apip.g9yx.com/h5/forward.html",
//                    "recharge_dsc_url": "https://apip.g9yx.com/h5/cz_instructions.html",
//                    "shop_url": "https://apip.g9yx.com/h5/forward.html",
//                    "reflect_dsc_url": "https://apip.g9yx.com/h5/tx_instructions.html",
//                    "vip_url": "https://apip.g9yx.com/h5/vip.html",
//                    "share_discount_url": "https://apip.g9yx.com/h5/share_zk.html",
//                    "account_guide_url": "https://apip.g9yx.com/h5/acount_guide.html",
//                    "expected_url": "https://apip.g9yx.com/h5/forward.html"

    public String shop_url;//	string	商城url
    public String activity_url;//	string	活动url
    public String market_url;//推广url
    public String vip_url;//vip通道url
    public String account_guide_url;//自动折扣攻略url
    public String share_discount_url;//	分享超低折扣url
    public String expected_url;//敬请期待url
    public String recharge_dsc_url;//充值说明
    public String reflect_dsc_url;//提现说明

}
