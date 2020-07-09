package com.zhny.library.presenter.monitor.custom.cluster;

import com.amap.api.maps.model.BitmapDescriptor;

/**
 * created by liming
 */
public interface ClusterRender {

    /**
     * 根据聚合点的元素数目返回渲染背景样式
     */
    BitmapDescriptor getDrawAble(Cluster cluster);
}
