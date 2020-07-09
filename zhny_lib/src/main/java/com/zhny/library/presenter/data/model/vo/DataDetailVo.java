package com.zhny.library.presenter.data.model.vo;

import java.io.Serializable;

/**
 * created by liming
 */
public class DataDetailVo implements Serializable {

    public int deviceId;
    public String name;
    public String sn;
    public String productType;
    public String imgUrl;
    public String areaStr; //作业面积
    public String mileageStr; //行驶里程
    public String workTime; //作业时长
    public String runningTime; //转运时长
    public String offLineTime;  //离线时长


}
