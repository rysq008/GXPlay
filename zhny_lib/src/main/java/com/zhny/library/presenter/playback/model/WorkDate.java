package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class WorkDate implements Serializable {

    @SerializedName("date")
    public String date;
    @SerializedName("content")
    public List<ContentBean> content;


    public static class ContentBean implements Serializable {
        @SerializedName("day")
        public String day;
        @SerializedName("detail")
        public List<DetailBean> detail;


        public static class DetailBean implements Serializable {

            @SerializedName("onlineDuration")
            public long onlineDuration;
            @SerializedName("runningDuration")
            public long runningDuration;
            @SerializedName("jobDuration")
            public long jobDuration;
            @SerializedName("offlineDuration")
            public long offlineDuration;
            @SerializedName("reportArea")
            public double reportArea;
            @SerializedName("mileage")
            public double mileage;
            @SerializedName("jobType")
            public String jobType;
            @SerializedName("rptDate")
            public String rptDate;

        }
    }
}
