package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-11 09:55
 */
public class DevicePeoperty implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

}
