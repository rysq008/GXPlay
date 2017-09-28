package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: LYB date: 2016/12/21.
 */
public class AppChannels implements Serializable {
  public static final int APP_LIST = 1;
  public static final int APP_TAB = 2;

  public long amount;
  public long commissionRate;
  public String desc;
  public String iconUrl;
  public String name;
  public long runningTime;
  public long channel;
  public int style;
}
