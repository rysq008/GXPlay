package com.zhny.library.presenter.playback.model;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class PlayInfo implements Serializable {

    public String type;//工作类型
    public long startPosition;// 开始位置
    public int allTime; //使用时长
    public List<LatLng> playPoints; //坐标集合

    public PlayInfo(String type, long startPosition, int allTime, List<LatLng> playPoints) {
        this.type = type;
        this.startPosition = startPosition;
        this.allTime = allTime;
        this.playPoints = playPoints;
    }

    @Override
    public String toString() {
        return "PlayInfo{" +
                "type='" + type + '\'' +
                ", startPosition=" + startPosition +
                ", allTime=" + allTime +
                ", playPoints=" + playPoints.size() +
                '}';
    }
}
