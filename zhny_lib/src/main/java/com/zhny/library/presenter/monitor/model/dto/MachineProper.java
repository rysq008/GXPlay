package com.zhny.library.presenter.monitor.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 设备实时属性数据
 *
 * created by liming
 */
public class MachineProper implements Serializable {


    /**
     * jobSummaryId : null
     * rptDate : null
     * deviceId : null
     * code : null
     * jobType : null
     * jobDuration : 0
     * onlineDuration : 0
     * reportArea : 0
     * countArea : 0
     * mileage : 0
     * offlineDuration : 46370782
     * runningDuration : 0
     * reportEfficiency : null
     * countEfficiency : null
     * getTodayStr : null
     * convertJobDuration : 0min
     * convertRunningDuration : 0min
     * convertOnlineDuration : 0min
     * convertOfflineDuration : 12h52min
     * convertReportEfficiency : null
     * convertCountEfficiency : null
     * convertReportArea : 0.0亩
     * convertCountArea : 0.0亩
     * convertMileage : 0.0km
     */


    @SerializedName("deviceId")
    public long deviceId;

    @SerializedName("jobDuration")
    public long jobDuration;

    @SerializedName("updatedTimestamp")
    public long updatedTimestamp;

    @SerializedName("convertJobDuration")
    public String convertJobDuration; //工作时间

    @SerializedName("convertRunningDuration")
    public String convertRunningDuration; //运转时间

    @SerializedName("convertOnlineDuration")
    public String convertOnlineDuration; //在线时间

    @SerializedName("convertOfflineDuration")
    public String convertOfflineDuration; //离线时间

    @SerializedName("convertReportEfficiency")
    public String convertReportEfficiency;

    @SerializedName("convertCountEfficiency")
    public String convertCountEfficiency;

    @SerializedName("convertReportArea")
    public String convertReportArea; //面积

    @SerializedName("convertCountArea")
    public String convertCountArea;

    @SerializedName("convertMileage")
    public String convertMileage; //里程

}
