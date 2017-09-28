package com.example.zr.gxapplication.net.model;

import android.util.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * author: zhoulei date: 15/11/24.
 */
// 账户信息
public class AccountInfo implements Serializable, Cloneable {
  private String nickName; // 昵称
  public String avatarUrl; // 头像url
  public long availableCash; // 可用余额
  public long totalProfit; // 总收益
  public long totalWithdraw; // 总提现
  public int totalUnusedRedPacketsCount; // 总未使用红包个数
  public long totalUnusedRedPacketsAmount; // 总红包金额
  public boolean hasAccount; // 是否有该账户
  public String errMsg; // 错误原因
  public boolean vip;

  public String getNickName() {
    byte[] bytes = new byte[0];
    try {
      bytes = nickName.getBytes("utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    byte[] byteArrays = Base64.decode(bytes, Base64.DEFAULT);
    return new String(byteArrays);
  }

  @Override
  public AccountInfo clone() {
    try {
      return (AccountInfo) super.clone();
    } catch (CloneNotSupportedException e) {
      AccountInfo accountInfo = new AccountInfo();
      accountInfo.nickName = nickName;
      accountInfo.avatarUrl = avatarUrl;
      accountInfo.availableCash = availableCash;
      accountInfo.totalProfit = totalProfit;
      accountInfo.totalWithdraw = totalWithdraw;
      accountInfo.hasAccount = hasAccount;
      accountInfo.errMsg = errMsg;
      return accountInfo;
    }
  }
}
