package com.zhny.zhny_app;

import com.amap.api.maps.model.LatLng;

public class MyLatLng {
    LatLng latLng;
    public static final int ABLE = 1;
    public static final int UNABLE = 2;
    int state = -1;

    MyLatLng(LatLng latLng2, int status) {
        this.latLng = latLng2;
        this.state = status;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
