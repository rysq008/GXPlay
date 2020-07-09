package com.zhny.library.presenter.work.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkDto implements Serializable {




    public String startTime2EndTime; //起始时间 - 结束时间   2020.01.01 - 2020.01.03

    public String startYear; //起始年份
    /**
     * jobId : null
     * sn : SN_7170508474
     * code : Task_20200214173816
     * messageType : 0
     * jobType : 0
     * jobTypeMeaning : 深松作业
     * rptDate : 2019-06-11 00:00:00
     * startTime : 2019-06-11 07:40:15
     * endTime : 2019-06-11 17:05:39
     * jobDuration : 13919000
     * width : 151.9
     * depth : 32.3
     * reportArea : 54.79
     * countArea : 34.5
     * mileage : 16418.6
     * status : 1
     * fieldId : 11945
     * longitude : 119
     * latitude : 40
     */

    @SerializedName("sn")
    public String sn;
    @SerializedName("code")
    public String code;
    @SerializedName("messageType")
    public int messageType;
    @SerializedName("jobType")
    public int jobType;
    @SerializedName("jobTypeMeaning")
    public String jobTypeMeaning;
    @SerializedName("rptDate")
    public String rptDate;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("jobDuration")
    public int jobDuration;
    @SerializedName("width")
    public double width;
    @SerializedName("depth")
    public double depth;
    @SerializedName("reportArea")
    public double reportArea;
    @SerializedName("countArea")
    public double countArea;
    @SerializedName("mileage")
    public double mileage;
    @SerializedName("status")
    public int status;
    @SerializedName("fieldId")
    public int fieldId;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("latitude")
    public double latitude;


    public WorkDto(int jobType, String jobTypeMeaning,String startTime,String endTime) {
        this.jobTypeMeaning =jobTypeMeaning;
        this.jobType = jobType;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
