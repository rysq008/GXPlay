package com.zhny.library.presenter.monitor.custom.cluster;

/**
 * created by liming
 */
public enum ClusterLevelEnum {

    COUNTRY(4, "国"),
    PROVINCE(6, "省"),
    CITY(8, "市"),
    DISTRICT(10, "区"),
    ;

    ClusterLevelEnum(float level, String levelName) {
        this.level = level;
        this.levelName = levelName;
    }

    public float level;
    public String levelName;

}
