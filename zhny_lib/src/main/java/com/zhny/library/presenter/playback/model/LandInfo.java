package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-12 12:58
 */

public class LandInfo implements Serializable {

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
//
    @SerializedName("fieldId")
    public String fieldId;
    @SerializedName("fieldCode")
    public String fieldCode;
    @SerializedName("fieldName")
    public String fieldName;
    @SerializedName("farmCode")
    public String farmCode;
    @SerializedName("coordinates")
    public String coordinates;
    @SerializedName("fieldArea")
    public String fieldArea;

    @SerializedName("center")
    public String center;

    @SerializedName("gravity")
    public String gravity;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("district")
    public String district;
    @SerializedName("type")
    public String type;

    @SerializedName("isDel")
    public int isDel;

    @SerializedName("organizationId")
    public int organizationId;

    @SerializedName("tenantId")
    public int tenantId;

    @SerializedName("tenanfarmtId")
    public List<FarmInfo> farm;


}
