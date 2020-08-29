package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

import java.util.List;

public class OrderResultBean extends XBaseModel {
    /**
     * couponDiscount : 0
     * amount : 1395.01
     * payableCode : http://sit.shop.chigoose.com/order/payment?orderSns=202009031013333&orderSns=202009031013334&
     * amountPayable : 1395
     * orderSns : ["202009031013333","202009031013334"]
     */

    public String couponDiscount;
    public String amount;
    public String payableCode;
    public String amountPayable;
    public List<String> orderSns;

    @Override
    public int itemType() {
        return 0;
    }
}
