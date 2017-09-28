package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/1.
 */
// 邀请页带参数二维码，以及订阅量
public class InvitePageQrcode implements Serializable {
  public String qrcodeUrl; // 分享链接Uri
  public String subscribeCount; // 订阅量
}
