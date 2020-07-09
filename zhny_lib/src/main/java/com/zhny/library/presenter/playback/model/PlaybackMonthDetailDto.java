package com.zhny.library.presenter.playback.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * description ： 农机轨迹日期返回实体
 * author : shd
 * date : 2020-02-07 10:06
 */
public class PlaybackMonthDetailDto implements Serializable {

    @SerializedName("type")
    public int type;//类型

    @SerializedName("startDate")
    public String startDate;//年月日

    @SerializedName("endDate")
    public String endDate;//年月日

    @SerializedName("month")
    public String month;//月份

    @SerializedName("date")
    public String date;//日期

    public boolean isOpen; //是否打开

    @SerializedName("detail")
    @Deprecated
    public WorkInfo.Detail detail;

    public WorkDate.ContentBean.DetailBean workDetail;

}
