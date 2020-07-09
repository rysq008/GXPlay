package com.zhny.library.presenter.work.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by liming
 */
public class SinglePicture implements Serializable {


    /**
     * url : https://zhny-dev-obs.obs.cn-north-4.myhuaweicloud.com/17aff984742211ea9f640b648f666e96.png
     * latitude : 35.07175268651702
     * longitude : 116.43821095456612
     */

    @SerializedName("url")
    public String url;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;

}
