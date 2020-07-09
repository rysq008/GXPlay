package com.zhny.library.presenter.monitor.custom.cluster;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class Cluster {

    public LatLng latLng;
    public String province;
    public String city;
    public String district;
    public String name;
    public long id;
    public boolean isSelected;
    public boolean isOnline;
    public List<ClusterItem> clusterItems;
    public Marker marker;


    public Cluster(LatLng latLng, String province, String city, String district, String name) {
        this.latLng = latLng;
        this.province = province;
        this.city = city;
        this.district = district;
        this.name = name;
        clusterItems = new ArrayList<>();
    }

    void addClusterItem(ClusterItem clusterItem) {
        clusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return clusterItems.size();
    }



}
