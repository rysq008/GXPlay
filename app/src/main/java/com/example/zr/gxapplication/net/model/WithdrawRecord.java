package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 提现记录
public class WithdrawRecord implements Serializable {
  public long withdrawAmount; // 提现金额
  public String withdrawDate; // 提现时间
}
