package com.zhny.library.presenter.machine.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 机具列表返回实体
 */

public class MachineDto implements Serializable {
    public String brand_model; //品牌-型号
    public String showTypeInList; //在机具列表展示的类型
    /**
     * deviceId : 49
     * name : 信翔真实设备695
     * productType : 拖拉机
     * productBrand : JohnDeal
     * productBrandMeaning : 约翰迪尔
     * productModel : 6J-2104
     * imgUrl : https://zhny-dev-obs.obs.cn-north-4.myhuaweicloud.com/1582286532963.jpg
     * sn : SN_7190904695
     * lpn : 冀E12345
     * jobType : null
     * jobTypeMeaning : null
     * tsn : null
     * province : 河北省
     * city : 邢台市
     * district : 宁晋县
     * latitude : 37.489986
     * longitude : 114.965904
     * status : true
     * alarm : []
     * productCategory : 1
     * productCategoryMeaning : 农机
     */

    @SerializedName("deviceId")
    public int deviceId;
    @SerializedName("name")
    public String name;
    @SerializedName("productType")
    public String productType;
    @SerializedName("productBrand")
    public String productBrand;
    @SerializedName("productBrandMeaning")
    public String productBrandMeaning;
    @SerializedName("productModel")
    public String productModel;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("sn")
    public String sn;
    @SerializedName("lpn")
    public String lpn;

    @SerializedName("tsn")
    public String tsn;
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
    @SerializedName("status")
    public boolean status;
    @SerializedName("productCategory")
    public String productCategory;
    @SerializedName("productCategoryMeaning")
    public String productCategoryMeaning;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public MachineDto(String name, String productType, String productCategory, String productCategoryMeaning) {
        this.name = name;
        this.productType = productType;
        this.productCategory = productCategory;
        this.productCategoryMeaning = productCategoryMeaning;
    }
}
