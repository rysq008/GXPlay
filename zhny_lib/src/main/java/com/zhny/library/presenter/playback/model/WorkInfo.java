package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-12 12:44
 */
public class WorkInfo implements Serializable {


    @SerializedName("date")
    public String date;
    @SerializedName("content")
    public List<Day> content;

    public class Day implements Serializable {
        @SerializedName("day")
        public String day;
        @SerializedName("detail")
        public List<Detail> detail;
    }

    public class Detail implements Serializable{
        @SerializedName("countArea")
        public String countArea;
        @SerializedName("mileage")
        public String mileage;
        @SerializedName("onlineTimestamp")
        public String onlineTimestamp;
        @SerializedName("jobTimestamp")
        public String jobTimestamp;
        @SerializedName("jobTime")
        public String jobTime;
        @SerializedName("runningTime")
        public String runningTime;
        @SerializedName("offLineTime")
        public String offLineTime;
        @SerializedName("jobTypes")
        public String jobTypes;
    }



}