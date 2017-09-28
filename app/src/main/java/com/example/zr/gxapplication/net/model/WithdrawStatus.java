package com.example.zr.gxapplication.net.model;

/**
 * author: zhoulei date: 15/12/1.
 */
// 提现状态
public enum WithdrawStatus {
  WITHDRAW_ENTRY, // 提现审核中
  WITHDRAW_APPROVED, // 提现通过
  WITHDRAW_REFUSED, // 提现拒绝
  WITHDRAW_SUCCESS // 提现成功
}
