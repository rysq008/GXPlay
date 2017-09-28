package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2017/3/21.
 */
public class AreaPackAllType implements Serializable {
  public int amount;
  public int commissionRate;
  public String desc;
  public int style;
  public String iconUrl;
  public String name;
  public int runningTime;
  public int channel;
  public int channel_type;
  public boolean isplayed;// 辅助判断（不属于返回数据）
  public int position;// 辅助索引（不属于返回数据）
  public boolean isshow;// 辅助判断是否已经显示（不属于返回数据）
}
