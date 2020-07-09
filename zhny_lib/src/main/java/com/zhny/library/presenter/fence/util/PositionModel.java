package com.zhny.library.presenter.fence.util;

/**
 * Created by zhengjiangrong on 2018/3/22.
 */

public class PositionModel {

    private double wgLat;
    private double wgLon;
    public PositionModel(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }
    public double getWgLat() {
        return wgLat;
    }
    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }
    public double getWgLon() {
        return wgLon;
    }
    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }
    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }
}
