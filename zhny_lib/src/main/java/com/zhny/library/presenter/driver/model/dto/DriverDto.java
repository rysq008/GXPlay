package com.zhny.library.presenter.driver.model.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DriverDto implements Serializable {
    public String firstName;
    /**
     * creationDate : null
     * createdBy : null
     * lastUpdateDate : null
     * lastUpdatedBy : null
     * objectVersionNumber : 96
     * workerId : 1
     * workerName : 测试66
     * workerCode : MJ0001
     * phoneNumber : 13996733333
     * isEnable : 0
     * remark : 123123123123
     * isDel : 0
     * organizationId : 1
     * tenantId : -1
     * type : null
     */

    @SerializedName("creationDate")
    public String creationDate;
    @SerializedName("createdBy")
    public String createdBy;
    @SerializedName("lastUpdateDate")
    public String lastUpdateDate;
    @SerializedName("lastUpdatedBy")
    public String lastUpdatedBy;
    @SerializedName("objectVersionNumber")
    public int objectVersionNumber;
    @SerializedName("workerId")
    public int workerId;
    @SerializedName("workerName")
    public String workerName;
    @SerializedName("workerCode")
    public String workerCode;
    @SerializedName("phoneNumber")
    public String phoneNumber;
    @SerializedName("isEnable")
    public int isEnable;
    @SerializedName("remark")
    public String remark;
    @SerializedName("isDel")
    public int isDel;
    @SerializedName("organizationId")
    public int organizationId;
    @SerializedName("tenantId")
    public int tenantId;

    @Override
    public String toString() {
        return "DriverDto{" +
                "creationDate='" + creationDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", workerId=" + workerId +
                ", workerName='" + workerName + '\'' +
                ", workerCode='" + workerCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isEnable=" + isEnable +
                ", remark='" + remark + '\'' +
                ", isDel=" + isDel +
                ", organizationId=" + organizationId +
                ", tenantId=" + tenantId +
                '}';
    }
}
