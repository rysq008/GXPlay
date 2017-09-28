package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/8.
 */
public class SendMsgRequestBody implements Serializable {
  public String mobile;

  public SendMsgRequestBody(String mobile) {
    this.mobile = mobile;
  }
}
