package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/3/21.
 */
public class CheckInResult implements Serializable {
  public int checkInFlag; // 签到标志位
  public boolean success; // 签到结果：成功或失败
  public String desc; // 描述
}
