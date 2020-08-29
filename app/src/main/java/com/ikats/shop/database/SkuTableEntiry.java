package com.ikats.shop.database;

import com.ikats.shop.model.BaseModel.XBaseModel;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;

@Entity
public final class SkuTableEntiry extends XBaseModel {

    @Id
    @NameInDb("_ID")
    public long _id;

    /**
     * "shopskucode": "",//销售商品SKU编码
     * "shopcode": "",//店铺编号
     * "shopclientcode": "",//销售组织编码
     * "shoppncode": "",//销售商品条码
     * "shopskuprice": "",//销售金额
     * "shopskuname": ""//销售商品名称
     * "shopchannelcode": "IKATS",
     * "shopskucode": "TEST20203201",
     * ║            "shopcode": "IKATS",
     * ║            "shopclientcode": "IKATS",
     * ║            "shoppncode": "TEST20203201",
     * ║            "shopskuprice": 11,
     * ║
     * ║            "shopskuname": "测试商品"
     */
    public String shopskucode;
    public String shopcode;
    public String shopclientcode;
    public String shoppncode;
    public String shopskuprice;
    public String shopskuname;
    public String shopchannelcode;//
}
