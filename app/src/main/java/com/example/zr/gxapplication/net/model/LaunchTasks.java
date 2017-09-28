package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * author: zhou date: 2016/5/17.
 */
public class LaunchTasks implements Serializable {
  public String desc;
  public List<LaunchTaskModel> today;
  public List<LaunchTaskModel> tomorrow;
}
