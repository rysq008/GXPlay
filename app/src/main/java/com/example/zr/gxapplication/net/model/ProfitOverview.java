package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 收益明细 各项汇总
public class ProfitOverview implements Serializable {
  public long totalProfit; // 累计总收益
  public long invitedProfit; // 邀请收益
  public long taskProfit; // 任务收益
  public long commisionProfit; // 任务提成收益
  public long subsBonus; // 关注公众号奖励
  public long scanProfit; // 扫码收益
  public long checkInProfit; // 签到收益汇总
  public long launchTaskProfit; // 追加任务收益
  public long newerTaskAwardProfit; //新手任务
  public long shareAwardProfit; //分享奖励
}
