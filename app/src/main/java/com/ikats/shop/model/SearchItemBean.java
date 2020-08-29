package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

public class SearchItemBean extends XBaseModel {

    public int index;
    public String order;
    public String sellid;
    public String count;
    public String amount;
    public String person;
    public String phone;
    public String status;
    public String createtime;
    public String action;
    public String starttime;
    public String endtime;

    @Override
    public int itemType() {
        return 0;
    }
}
