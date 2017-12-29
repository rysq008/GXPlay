package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2017/12/28.
 */

public class H5UrlListResults extends XBaseModel {

    /**
     * market_url : http://60.205.204.218:8080/G9/promotion_sy.html
     * vip_url : http://60.205.204.218:8080/G9/vip.html
     */

    private String market_url;
    private String vip_url;
    private String account_guide_url;

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

    public String getAccount_guide_url() {
        return account_guide_url;
    }

    public void setAccount_guide_url(String account_guide_url) {
        this.account_guide_url = account_guide_url;
    }
}
