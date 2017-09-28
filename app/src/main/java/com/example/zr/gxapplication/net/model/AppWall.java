package com.example.zr.gxapplication.net.model;

import com.shandianshua.totoro.event.model.Channel;

import java.io.Serializable;

/**
 * author: zhou date: 2016/1/20.
 */
// 应用墙
public class AppWall implements Serializable {
  public String name; // 名称
  public Channel channel; // 应用渠道
  public long amount; // 任务奖励金额
  public long commissionRate; // 徒弟提成费率
  public long runningTime; // 有效运行时长
  public String iconUrl; // icon地址
  public String desc; // 应用墙描述
}
