package com.game.helper.model.model;

import com.game.helper.model.BaseModel.XBaseModel;
import com.game.helper.net.model.WeiXinPayRequestBody;

public class PayResultModel extends XBaseModel {
    public String orderInfo;
    public String tradeNo;
    public WeiXinPayRequestBody wxorderInfo;
    public String payChannel;

    public PayResultModel() {
    }

    public PayResultModel(int page, String orderInfo, String tradeNo, WeiXinPayRequestBody wxorderInfo, String payChannel) {
        this.orderInfo = orderInfo;
        this.tradeNo = tradeNo;
        this.wxorderInfo = wxorderInfo;
        this.payChannel = payChannel;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public WeiXinPayRequestBody getWxorderInfo() {
        return wxorderInfo;
    }

    public void setWxorderInfo(WeiXinPayRequestBody wxorderInfo) {
        this.wxorderInfo = wxorderInfo;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

}
