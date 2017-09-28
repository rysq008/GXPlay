package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/6/1.
 */
public class WithdrawRedPacket implements Serializable {
  public long id;
  public long amount;
  public long minWithdrawAmount;

  public String name;
  public String desc;
  public String disableDesc;
}
