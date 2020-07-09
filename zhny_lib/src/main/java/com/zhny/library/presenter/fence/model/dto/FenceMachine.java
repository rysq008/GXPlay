package com.zhny.library.presenter.fence.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by liming
 */
public class FenceMachine implements Serializable {

    /**
     * sn : JZH019298378743
     * name : 信翔联调测试设备
     * associatedFlag : false
     * internalFlag : true
     * productType : 插秧机
     * productModel : WLD-1200型双圆盘
     * productBrand : JohnDeal
     * productBrandMeaning : 约翰迪尔
     * imgUrl :
     */

    @SerializedName("deviceId")
    public int deviceId;
    @SerializedName("sn")
    public String sn;
    @SerializedName("name")
    public String name;
    @SerializedName("associatedFlag")
    public boolean associatedFlag;
    @SerializedName("selfAssociatedFlag")
    public boolean selfAssociatedFlag;
    @SerializedName("internalFlag")
    public boolean internalFlag;
    @SerializedName("productType")
    public String productType;
    @SerializedName("productModel")
    public String productModel;
    @SerializedName("productBrand")
    public String productBrand;
    @SerializedName("productBrandMeaning")
    public String productBrandMeaning;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("district")
    public String district;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;


    public boolean outedFence; //是否围栏外

    public boolean linkedFence; //是否已关联

    public String brandAndModel; //品牌-型号

    public int checkType; //-1 : 不可选 0：没有选择 1:已选择


}
