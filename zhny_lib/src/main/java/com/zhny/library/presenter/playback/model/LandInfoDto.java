package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-12 12:58
 */

public class LandInfoDto implements Serializable {

    @SerializedName("totalPages")
    public String totalPages;
    @SerializedName("totalElements")
    public String totalElements;
    @SerializedName("numberOfElements")
    public String numberOfElements;
    @SerializedName("size")
    public String size;
    @SerializedName("number")
    public String number;
    @SerializedName("content")
    public ArrayList<LandInfo> content;

    @SerializedName("empty")
    public String empty;


}
