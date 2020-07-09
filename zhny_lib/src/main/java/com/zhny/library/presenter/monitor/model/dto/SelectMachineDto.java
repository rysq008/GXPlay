package com.zhny.library.presenter.monitor.model.dto;

import com.amap.api.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by liming
 */
public class SelectMachineDto implements Serializable {


    /**
     * deviceId : 22
     * name : 信翔联调测试设备
     * productType : 撒肥机
     * productBrand : 约翰迪尔
     * productModel : WLD-1200型双圆盘
     * imgUrl : null
     * sn : JZH019298378743
     * lpn : null
     * jobType : null
     * tsn : null
     * province : null
     * city : null
     * district : null
     * latitude : 35.01862
     * longitude : 116.431335
     * status : false
     */


    @SerializedName("deviceId")
    public long deviceId;

    @SerializedName("name")
    public String name;

    @SerializedName("productType")
    public String productType;

    @SerializedName("productBrandMeaning")
    public String productBrandMeaning;

    @SerializedName("productModel")
    public String productModel;

    @SerializedName("imgUrl")
    public String imgUrl;

    @SerializedName("sn")
    public String sn;

    @SerializedName("jobType")
    public String jobType;

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

    public LatLng latLng; //业务LatLng

    public String brandAndModel; //品牌-型号

    public String address; //省市区

    public boolean selected; //业务selected

}
