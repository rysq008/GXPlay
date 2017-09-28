package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 验证手机验证码request model
public class MsgVerifyRequestModel implements Serializable {

  public String mobile; // 手机号
  public long verifyCode; // 短信验证码
  public String weChatId; // 微信号


  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public void setVerifyCode(long verifyCode) {
    this.verifyCode = verifyCode;
  }

  public void setWeChatId(String weChatId) {
    this.weChatId = weChatId;
  }
}
