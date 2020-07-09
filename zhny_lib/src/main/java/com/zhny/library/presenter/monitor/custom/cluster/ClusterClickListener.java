package com.zhny.library.presenter.monitor.custom.cluster;

import com.amap.api.maps.model.Marker;

/**
 * created by liming
 */
public interface ClusterClickListener {

    /**
     * 点击聚合点的回调处理函数
     */
    void onClusterClick(Marker marker, Cluster clusterItems);


    /**
     * mapZoom 变化回调
     */
    void onCameraChangeFinish(float mapZoom);

}
