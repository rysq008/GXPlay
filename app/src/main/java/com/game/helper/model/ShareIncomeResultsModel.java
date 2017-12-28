package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

public class ShareIncomeResultsModel extends XBaseModel {

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

    public String market_url;
    public String vip_url;


    @Override
    public int itemType() {
            return 0;
        }
}
