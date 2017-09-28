package com.example.zr.gxapplication.net.model;

import com.shandianshua.totoro.event.model.Channel;

import java.io.Serializable;

/**
 * author: zhou date: 2016/5/18.
 */
public class LaunchEventModel implements Serializable {
  public String packageName;
  public String appName;
  public String versionName;
  public Channel channel;
  public long eventTime;
  public String eventId;
  public long appRunningTime;
  public String otherInfo;
  public String type;
}
