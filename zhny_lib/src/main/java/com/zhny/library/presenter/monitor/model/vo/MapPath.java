package com.zhny.library.presenter.monitor.model.vo;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * 地块绘制类
 *
 * created by liming
 */
public class MapPath implements Serializable {

    public Projection projection;
    public String coordinates;
    public List<LatLng> latLngs;

    public MapPath() {
    }

    public MapPath(Projection projection, List<LatLng> latLngs) {
        this.projection = projection;
        this.latLngs = latLngs;
    }

    public MapPath(Projection projection, String coordinates) {
        this.projection = projection;
        this.coordinates = coordinates;
    }
}
