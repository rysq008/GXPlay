package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * author: zhou date: 2016/5/9.
 */
public class BroadcastAdItems implements Serializable {
  public String code;
  public String message;

  public List<BroadcastAdItem> result;
  public boolean launchTasks;

  public BroadcastAdItems(List<BroadcastAdItem> broadcastAdItems,
      boolean launchTasks) {
    this.result = broadcastAdItems;
    this.launchTasks = launchTasks;
  }
}
