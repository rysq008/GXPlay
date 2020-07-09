package com.zhny.library.presenter.playback.model;

import java.io.Serializable;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-08 21:53
 */
public class ColorInfo implements Serializable {

    public int colorInt;
    public long from;
    public long to;
    public String desc;

    public ColorInfo() {
    }


    public ColorInfo(int colorInt, long from, long to, String desc) {
        this.colorInt = colorInt;
        this.from = from;
        this.to = to;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ColorInfo{" +
                "colorInt=" + colorInt +
                ", from=" + from +
                ", to=" + to +
                ", desc='" + desc + '\'' +
                '}';
    }
}
