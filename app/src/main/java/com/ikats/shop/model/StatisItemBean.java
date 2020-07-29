package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

public class StatisItemBean extends XBaseModel {

    public String sort;
    public String goods;
    public String qrcode;
    public String sellAmount;
    public String sellCount;

    @Override
    public int itemType() {
        return 0;
    }
}
