package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * author: zhou date: 2016/3/7.
 */
public class PosterData implements Serializable {
  public int version;
  public List<PosterModel> posterModels;

  public static class PosterModel implements Serializable {
    public String url;
    public List<PosterConfigInfo> avatarConfigInfo;
    public List<PosterConfigInfo> qrcodeConfigInfo;

    public static class PosterConfigInfo implements Serializable {
      public boolean roundShape; // 是否为圆形图片，目前只对avatarCoordInfo有效
      public String color; // 二维码默认颜色， 目前只对qrcodeCoordInfo有效
      public float coordX; // 坐标x在海报中的百分比
      public float coordY; // 坐标y在海报中的百分比
      public float coordW; // 头像图片宽度在海报中的百分比
    }
  }
}
