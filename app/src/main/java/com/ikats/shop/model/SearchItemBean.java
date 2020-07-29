package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

public class SearchItemBean extends XBaseModel {

    public String sort;
    public String order;
    public String count;
    public String amount;
    public String person;
    public String phone;
    public String status;
    public String createtime;
    public String action;

    @Override
    public int itemType() {
        return 0;
    }
}
