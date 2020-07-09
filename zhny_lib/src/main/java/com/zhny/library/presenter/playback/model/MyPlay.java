package com.zhny.library.presenter.playback.model;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class MyPlay implements Serializable {

    public String workState;
    public long startPosition;
    public long endPosition;
    public List<LatLng> points;

    public MyPlay(String workState, long startPosition, long endPosition, List<LatLng> points) {
        this.workState = workState;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.points = points;
    }

    @Override
    public String toString() {
        return "MyPlay{" +
                "workState='" + workState + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", points=" + points.size() +
                '}';
    }
}
