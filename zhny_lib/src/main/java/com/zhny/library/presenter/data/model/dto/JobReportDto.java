package com.zhny.library.presenter.data.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class JobReportDto implements Serializable {


    /**
     * sumArea : 152.12
     * aveArea : 25.35
     * sumWorkTime : 39135000
     * jobList : [{"deviceId":48,"sn":"SN_7190411453","name":"信翔真实设备453","area":92.59,"areaProportion":null,"mileage":35453.8,"workTime":24618000,"runningTime":4065000,"offLineTime":619316000,"offLineTimeProportion":0.96,"workTimeProportion":0.03,"runningTimeProportion":0.01,"sumArea":152.12}]
     */

    @SerializedName("sumArea")
    public double sumArea;
    @SerializedName("aveArea")
    public double aveArea;
    @SerializedName("sumWorkTime")
    public int sumWorkTime;
    @SerializedName("jobList")
    public List<JobBean> jobList;


    public static class JobBean implements Serializable {
        /**
         * deviceId : 48
         * sn : SN_7190411453
         * name : 信翔真实设备453
         * area : 92.59
         * areaProportion : null
         * mileage : 35453.8
         * workTime : 24618000
         * runningTime : 4065000
         * offLineTime : 619316000
         * offLineTimeProportion : 0.96
         * workTimeProportion : 0.03
         * runningTimeProportion : 0.01
         * sumArea : 152.12
         */

        @SerializedName("deviceId")
        public int deviceId;
        @SerializedName("sn")
        public String sn;
        @SerializedName("name")
        public String name;  //设备名称
        @SerializedName("productType")
        public String productType;
        @SerializedName("imgUrl")
        public String imgUrl;
        @SerializedName("productBrand")
        public String productBrand;
        @SerializedName("productBrandMeaning")
        public String productBrandMeaning;
        @SerializedName("productModel")
        public String productModel;
        @SerializedName("area")
        public double area; //作业面积
        @SerializedName("areaProportion")
        public double areaProportion; //作业面积比
        @SerializedName("mileage")
        public double mileage; //行驶里程
        @SerializedName("workTime")
        public long workTime; //作业时长
        @SerializedName("runningTime")
        public long runningTime; //转运时长
        @SerializedName("offLineTime")
        public long offLineTime;  //离线时长
        @SerializedName("offLineTimeProportion")
        public double offLineTimeProportion;
        @SerializedName("workTimeProportion")
        public double workTimeProportion;
        @SerializedName("runningTimeProportion")
        public double runningTimeProportion;
        @SerializedName("sumArea")
        public double sumArea;

    }
}
