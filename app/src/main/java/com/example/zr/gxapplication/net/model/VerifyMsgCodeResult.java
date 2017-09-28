package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 验证短信验证码结果
public class VerifyMsgCodeResult implements Serializable {
  public boolean success; // 是否验证成功
  public String desc; // 结果描述
}
