package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

import java.io.Serializable;
import java.util.List;

public class PayResultBean extends XBaseModel {

    /**
     * amount : 8.39
     * isPaySuccess : 1
     * skus : [{"quantity":1,"price":8.39,"name":"资生堂(SHISEIDO) uno吾诺男士洗面奶 黑色款 130g/支 控油清爽平衡深层保湿洁面","skuSn":"4901872449675"}]
     * orderSn : 202009191010924
     * paymentMethod : 微信支付(小程序支付)
     */

    public double amount;
    public int isPaySuccess;
    public String orderSn;
    public String paymentMethod;
    public List<SkusBean> skus;


    public static class SkusBean implements Serializable {
        /**
         * quantity : 1
         * price : 8.39
         * name : 资生堂(SHISEIDO) uno吾诺男士洗面奶 黑色款 130g/支 控油清爽平衡深层保湿洁面
         * skuSn : 4901872449675
         */

        public int quantity;
        public double price;
        public String name;
        public String skuSn;
    }
}
