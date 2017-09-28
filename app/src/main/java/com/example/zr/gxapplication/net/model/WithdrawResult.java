package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/11/24.
 */
// 提现结果
public class WithdrawResult implements Serializable {
  public boolean success; // 是否提现成功
  public String desc; // 提现结果描述
}
