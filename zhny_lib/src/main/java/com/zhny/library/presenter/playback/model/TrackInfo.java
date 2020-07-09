package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-11 12:58
 */

public class TrackInfo implements Serializable {

    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("trackDate")
    public String trackDate;
    @SerializedName("productBrandDetail")
    public String productBrandDetail;
    @SerializedName("speed")
    public String speed;
    @SerializedName("width")
    public float width;
    @SerializedName("jobTypeDetail")
    public String jobTypeDetail;
    @SerializedName("state")
    public String state;
    @SerializedName("workingState")
    public String workingState;

    public long position;



    @Override
    public String toString() {
        return "TrackInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", trackDate='" + trackDate + '\'' +
                ", productBrandDetail='" + productBrandDetail + '\'' +
                ", speed='" + speed + '\'' +
                ", width='" + width + '\'' +
                ", jobTypeDetail='" + jobTypeDetail + '\'' +
                ", state='" + state + '\'' +
                ", workingState='" + workingState + '\'' +
                '}';
    }
}
