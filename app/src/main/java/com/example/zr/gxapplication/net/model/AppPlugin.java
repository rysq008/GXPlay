package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/1/20.
 */
// 渠道包
public class AppPlugin implements Serializable {
  public String name; // 名称
  public String packageName; // 包名
  public String versionName; // 版本
  public String iconUrl; // icon地址
  public String downloadUrl; // 下载地址
  public String desc; // 描述

  public long sort; // 排序
}
