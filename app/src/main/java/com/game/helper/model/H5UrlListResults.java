package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2017/12/28.
 */

public class H5UrlListResults extends XBaseModel {
    
    public String getMarket_url() {
        return market_url;
    }

    public void setMarket_url(String market_url) {
        this.market_url = market_url;
    }

    public String getVip_url() {
        return vip_url;
    }

    public void setVip_url(String vip_url) {
        this.vip_url = vip_url;
    }

    public String market_url;//推广url
    public String vip_url;//vip通道url
    public String account_guide_url;//自动折扣攻略url
    public String share_discount_url;//	分享超低折扣url
    public String expected_url;//敬请期待url

    public String getExpected_url() {
        return expected_url;
    }

    public void setExpected_url(String expected_url) {
        this.expected_url = expected_url;
    }

    public String getShare_discount_url() {
        return share_discount_url;
    }

    public void setShare_discount_url(String share_discount_url) {
        this.share_discount_url = share_discount_url;
    }

    public String getAccount_guide_url() {
        return account_guide_url;
    }

    public void setAccount_guide_url(String account_guide_url) {
        this.account_guide_url = account_guide_url;
    }
}
