package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 首次提现发送验证码结果
public class SendMsgVerifyCodeResult implements Serializable {
  public static final int SUCCESS_CODE = 0;

  public int code; // 如果不为0，失败
  public String desc; // 描述
}
