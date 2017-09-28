package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/1/20.
 */
// 渠道包
public class AppPackage implements Serializable {
  public String name;
  public String packageName;
  public String versionName;
  public long amount;
  public long commissionRate;
  public long runningTime;
  public String iconUrl;
  public String downloadUrl;
  public String desc;
  public long channel;
  public String id_pass_back;
}
