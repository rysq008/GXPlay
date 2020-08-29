package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

public class BackstageGoodsItemBean extends XBaseModel {

    public int index;
    public String name;
    public String barcode;
    public String code;
    public String unit;
    public String price;
    public String place;
    public String brank;
    public String spec;
    public String rough_weight;
    public String net_weight;
    @Override
    public int itemType() {
        return 0;
    }
}
