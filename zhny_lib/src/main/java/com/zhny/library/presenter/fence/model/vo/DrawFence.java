package com.zhny.library.presenter.fence.model.vo;

import com.amap.api.maps.model.Marker;

import java.io.Serializable;

/**
 * 围栏绘制辅助类
 *
 * created by liming
 */
public class DrawFence implements Serializable {

    public DrawFence(String markerId, Marker marker) {
        this.markerId = markerId;
        this.marker = marker;
    }

    public String markerId;
    public Marker marker;

}
