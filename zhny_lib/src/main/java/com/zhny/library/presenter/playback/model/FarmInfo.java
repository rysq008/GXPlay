package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-12 12:44
 */
public class FarmInfo implements Serializable {
    @SerializedName("creationDate")
    public String creationDate;

    @SerializedName("createdBy")
    public String createdBy;
    @SerializedName("lastUpdateDate")
    public String lastUpdateDate;
    @SerializedName("lastUpdatedBy")
    public String lastUpdatedBy;

    @SerializedName("objectVersionNumber")
    public String objectVersionNumber;
    @SerializedName("farmId")
    public String farmId;
    @SerializedName("farmCode")
    public String farmCode;
    @SerializedName("farmName")
    public String farmName;
    @SerializedName("userName")
    public String userName;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("latitud")
    public String latitud;
    @SerializedName("isDel")
    public String isDel;
    @SerializedName("organizationId")
    public String organizationId;
    @SerializedName("tenantId")
    public String tenantId;
    @SerializedName("fieldList")
    public String fieldList;


}