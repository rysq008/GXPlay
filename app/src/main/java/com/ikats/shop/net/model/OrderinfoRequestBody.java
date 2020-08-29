package com.ikats.shop.net.model;

import android.text.TextUtils;

import com.ikats.shop.model.GoodsBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.kit.Kits;

public class OrderinfoRequestBody extends BaseRequestBody {

    /**
     * {
     * "skuNos":[
     * {
     * "skuNo":"2017122720345_6",//商品编号
     * "quantity":"1"//数量
     * },
     * {
     * "skuNo":"2017122720365_2",
     * "quantity":"1"
     * }
     * ],
     * "receiverId":302,//这个信息从获取用户信息的接口拿到receivers里面的id
     * "currentUser":{
     * "mobile":"13121528060"//当前用户手机号
     * },
     * "receiver":{
     * "consignee":"李四",//收货人
     * "areaName":"北京市东城区",//地区名称
     * "address":"望京SOHOT1C903",//地址
     * "zipCode":"100000",//邮编
     * "phone":"13121528060"//电话
     * },
     * "paymentMethodId":1,//写死
     * "shippingMethodId":1//写死
     * }
     */

    public String paymentMethodId = String.valueOf(1);//,//写死
    public String shippingMethodId = String.valueOf(1);//写死
    public String receiverId = String.valueOf(302);
    public String code = "";//优惠码
    public Map<String, Object> currentUser = new HashMap<>();
    public Map<String, Object> receiver = new HashMap<>();
    public List<Map<String, Object>> skuNos = new ArrayList<>();
    static OrderinfoRequestBody orderinfoRequestBody = null;
    public String posCode = "";
    public String sellCode = "";

    public String channelCode;// 渠道编码
    public String channelName;// 渠道名称

    public OrderinfoRequestBody(String businessCode, String tid) {
        super(1);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        Builder() {
            orderinfoRequestBody = null;
            orderinfoRequestBody = new OrderinfoRequestBody("", "");
            orderinfoRequestBody.currentUser.clear();
            orderinfoRequestBody.skuNos.clear();
            orderinfoRequestBody.receiver.clear();
        }

        public Builder setPaymentMethodId(String paymentMethodId) {
            orderinfoRequestBody.paymentMethodId = paymentMethodId;
            return this;
        }

        public Builder setShippingMethodId(String shippingMethodId) {
            orderinfoRequestBody.shippingMethodId = shippingMethodId;
            return this;
        }

        public Builder setReceiverId(String receiverId) {
            orderinfoRequestBody.receiverId = receiverId;
            return this;
        }

        public Builder setCurrentUser(Map<String, Object> currentUser) {
            orderinfoRequestBody.currentUser = currentUser;
            return this;
        }

        public Builder setCurrentUser(String key, Object val) {
            orderinfoRequestBody.currentUser.put(key, val);
            return this;
        }

        public Builder setReceiver(boolean by_express, String consignee, String areaName, String address, String zipCode, String phone) {
            if (!by_express) {
                Map map = new HashMap();
                map.put("consignee", TextUtils.isEmpty(consignee) ? "客户" : consignee);//收货人
                map.put("areaName", areaName);//地区名称
                map.put("address", address);//地址
                map.put("zipCode", zipCode);//邮编
                map.put("phone", phone);//电话
                orderinfoRequestBody.receiver = map;
            }
            return this;
        }

        public Builder setSkuNos(List<GoodsBean> skuNos) {
            if (!Kits.Empty.check(skuNos))
                for (GoodsBean bean : skuNos) {
                    Map map = new HashMap();
                    map.put("skuNo", bean.productId);
                    map.put("quantity", bean.count);
                    orderinfoRequestBody.skuNos.add(map);
                }
            return this;
        }

        public Builder setSkuNos(Map<String, Object>... skuNos) {
            orderinfoRequestBody.skuNos.addAll(Arrays.asList(skuNos));
            return this;
        }

        public Builder setOrderinfoRequestBody(OrderinfoRequestBody payinfoRequestBody) {
            OrderinfoRequestBody.orderinfoRequestBody = payinfoRequestBody;
            return this;
        }

        public Builder setCode(String code) {
            orderinfoRequestBody.code = code;
            return this;
        }

        public Builder setPosCode(String posCode) {
            orderinfoRequestBody.posCode = posCode;
            return this;
        }

        public Builder setSellCode(String sellCode) {
            orderinfoRequestBody.sellCode = sellCode;
            return this;
        }

        public Builder setChannelCode(String channelCode) {
            orderinfoRequestBody.channelCode = channelCode;
            return this;
        }

        public Builder setChannelName(String channelName) {
            orderinfoRequestBody.channelName = channelName;
            return this;
        }


        public OrderinfoRequestBody builder() {
            if (orderinfoRequestBody.receiver.size() > 0) {
                orderinfoRequestBody.receiverId = "";
            }
            return orderinfoRequestBody;
        }
    }

}
