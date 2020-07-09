package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * description ： 农机列表返回实体
 * author : shd
 * date : 2020-02-07 10:06
 */
public class PlaybackInfoDto implements Serializable {
//
//    public String imageUrl;//图片url
//    public String name;//名称
//    public String brand;//品牌
//    public String model;//型号
//    public String type;//农机类型
    @SerializedName("brand_model")
    public String brand_model; //品牌-型号

    @SerializedName("deviceId")
    public String deviceId;

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
    @SerializedName("outDate")
    public String outDate;
    @SerializedName("sn")
    public String sn;
    @SerializedName("lpn")
    public String lpn;
    @SerializedName("propertyList")
    public List<DevicePeoperty> propertyList;
    @SerializedName("jobType")
    public String jobType;
    @SerializedName("tsn")
    public String tsn;


    @Override
    public String toString() {
        return "PlaybackInfoDto{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", productType='" + productType + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productModel='" + productModel + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", outDate='" + outDate + '\'' +
                ", sn='" + sn + '\'' +
                ", lpn='" + lpn + '\'' +
                ", propertyList=" + propertyList +
                ", jobType='" + jobType + '\'' +
                ", tsn='" + tsn + '\'' +
                '}';
    }
}
