package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/5/17.
 */
public class LaunchTaskModel implements Serializable {
  public String package_name;
  public int channel;
  public long amount;
  public long install_time;
  public long running_time;
  public int interval;
}
