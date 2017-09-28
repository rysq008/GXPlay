package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 邀请收益每天汇总
public class InviteProfitDailyOverview implements Serializable {
  public String profitDate; // 收益日期
  public long profitAmount; // 收益金额
}
