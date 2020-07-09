package com.zhny.library.presenter.data.model.vo;

import java.io.Serializable;

/**
 * created by liming
 */
public class PieDataVo implements Serializable {

    public long workingTime;
    public long runningTime;
    public long offlineTime;

    public PieDataVo(long workingTime, long runningTime, long offlineTime) {
        this.workingTime = workingTime;
        this.runningTime = runningTime;
        this.offlineTime = offlineTime;
    }

}
