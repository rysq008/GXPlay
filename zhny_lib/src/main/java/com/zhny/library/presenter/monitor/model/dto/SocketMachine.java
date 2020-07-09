package com.zhny.library.presenter.monitor.model.dto;

import java.io.Serializable;

/**
 * 农机实时监控数据
 *
 * created by liming
 */
public class SocketMachine implements Serializable {


    /**
     * altitude : 0
     * latitude : 35.01862
     * positionWay : 0
     * location : 山东省济宁市鱼台县李阁镇李淳线
     * gpstime : 2019-10-03 14:45:23
     * speed : 0
     * direction : 0
     * longitude : 116.531335
     */

    public String altitude;
    public String positionWay;
    public String location;
    public String gpstime;
    public int speed;
    public int direction;
    public double latitude;
    public double longitude;

}
