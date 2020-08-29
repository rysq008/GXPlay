package com.ikats.shop.model;


import java.io.Serializable;
import java.util.List;

public class PrintBean implements Serializable {
    public String shop_name;
    public String shop_code;
    public String sell_code;
    public String cashier;
    public List<GoodsBean> list;
    public String total;
    public String discounts;
    public String cope;
    public String paid;
}
