package com.zhny.library.presenter.monitor.custom.cluster;

import com.amap.api.maps.model.LatLng;

/**
 * created by liming
 */
public class RegionItem implements ClusterItem {

    public LatLng latLng;
    public String name;
    public String province;
    public String city;
    public String district;
    public boolean isOnline;
    public boolean isSelected;
    public long id;


    public RegionItem(LatLng latLng, String name, String province, String city, String district) {
        this.latLng = latLng;
        this.name = name;
        this.province = province;
        this.city = city;
        this.district = district;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getProvince() {
        return province;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getDistrict() {
        return district;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
