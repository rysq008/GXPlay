package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

public class AppIncomeBanner extends BaseModel {

  public List<AppIncomeBannerModel> list;

  public class AppIncomeBannerModel implements Serializable {
    public String title;// : String 标题
    public String image;// : String 图片地址
    public String url;// String 图片链接
  }
}
