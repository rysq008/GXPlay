package com.ikats.shop.model;


import com.ikats.shop.model.BaseModel.XBaseModel;

public class GoodsBean extends XBaseModel {
    public String productId;
    public String barcode;
    public String shopchannelcode;
    public String shopcode;
    public String url;
    public String skuNo = "";
    public int count;
    public String name;
    public float origin_price = 1.25f;
    public float sell_price = 0.95f;
    public float amount;
//    public int pos_in_list;//辅助判断在购物清单中的位置


}
