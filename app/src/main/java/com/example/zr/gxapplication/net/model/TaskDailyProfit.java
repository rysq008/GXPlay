package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 每日邀请收益汇总
public class TaskDailyProfit implements Serializable {
  public String profitDate; // 收益日期
  public long profitAmount; // 收益金额
}
