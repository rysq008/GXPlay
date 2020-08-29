package com.ikats.shop.net.model;

public class CancelOrderRequestBody extends BaseRequestBody {


    public String orderSn;//:"202009191010919"

    public CancelOrderRequestBody(String orderCode) {
        super(1);
        this.orderSn = orderCode;
    }

}
