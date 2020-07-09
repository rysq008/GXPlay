package com.zhny.library.presenter.fence.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class FenceVo implements Serializable {


    /**
     * objectVersionNumber : 9
     * geofenceId : 39
     * name : 电子围栏02
     * center : null
     * radius : null
     * playPoints : ["116.361179,39.923166,39.923165600420184,116.36117913186547;116.367187,39.914345,39.91434460989358,116.36718728005883;116.397056,39.919216,39.919216043650735,116.39705635964867","115.945587,39.915613,39.91561332157847,115.94558693960312;115.757446,39.669755,39.669754712387906,115.75744607046249;116.225738,39.615823,39.615822987830434,116.22573830679062","103.422546,40.984887,40.98488717359252,103.42254616409537;97.753601,35.716204,35.716203873377076,97.75360085159537;108.432312,33.546903,33.54690289333747,108.43231178909537","115.0284,34.026628,34.02662810861042,115.02840020254257;119.675617,36.121376,36.12137550633373,119.67561699941757;115.0284,37.108997,37.10899710522703,115.02840020254257"]
     * remark : 测试修改3
     * deviceSns : ["JZH019298378743","SN_7190411453"]
     */

    public Integer objectVersionNumber;
    public Integer geofenceId;
    public String name;
    public String center;
    public String points;
    public List<String> pointList;
    public String remark;
    public List<String> deviceSns;

    public FenceVo() {
    }

    public FenceVo(Integer objectVersionNumber, Integer geofenceId, String name) {
        this.objectVersionNumber = objectVersionNumber;
        this.geofenceId = geofenceId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FenceVo{" +
                "objectVersionNumber=" + objectVersionNumber +
                ", geofenceId=" + geofenceId +
                ", name='" + name + '\'' +
                ", center='" + center + '\'' +
                ", playPoints='" + points + '\'' +
                ", pointList=" + pointList +
                ", remark='" + remark + '\'' +
                ", deviceSns=" + deviceSns +
                '}';
    }
}
