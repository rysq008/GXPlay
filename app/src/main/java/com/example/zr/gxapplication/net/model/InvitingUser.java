package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 正在邀请中的用户
public class InvitingUser implements Serializable {
  public String unionId; // 用户微信unionId
  public String nickName; // 用户微信昵称
  public long invitingTime; // 邀请开始时间
}
