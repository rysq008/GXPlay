package com.ikats.shop.net.model;

public class SkuDataRequestBody extends BaseRequestBody {

    public String shopCode;//
    public String shopChannelCode;//

    public SkuDataRequestBody(String shopCode) {
        super(0);
        this.shopCode = shopCode;
        this.shopChannelCode = shopCode;
    }
}
