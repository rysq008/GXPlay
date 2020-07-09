package com.zhny.library.presenter.monitor.custom.cluster;

import com.amap.api.maps.model.LatLng;

/**
 * created by liming
 */
public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     */
    LatLng getPosition();

    String getProvince();

    String getCity();

    String getDistrict();

    String getName();

    boolean isOnline();

    boolean isSelected();

    long getId();

    void setSelected(boolean isSelected);

}
