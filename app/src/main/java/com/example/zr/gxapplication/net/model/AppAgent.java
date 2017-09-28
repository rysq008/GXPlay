package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: lyb date: 2016/7/16.
 */
// 特工任务
public class AppAgent extends BaseModel implements Serializable {
  public String name; // 名称
  public String id; // 特工任务编号
  public String iconUrl; // icon地址
  public String desc; // 任务描述
}
