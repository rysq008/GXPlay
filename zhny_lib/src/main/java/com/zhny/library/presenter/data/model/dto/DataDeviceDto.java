package com.zhny.library.presenter.data.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 数据统计查询设备
 *
 * created by liming
 */
public class DataDeviceDto implements Serializable {

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

    public int checkType; // 0：未选中，1：选中
    public String brandAndModel;

}
