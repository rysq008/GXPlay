package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by wangsongyan on 15/12/6.
 * Email: wangsongyan@shandianshua.com
 */
// 用户是否关注公众号
public class FollowResult implements Serializable {
  public boolean exists; // unionId是否存在
  public String errDesc; // 错误描述
  public String searchDesc; // 搜索关注文案
  public String qrcodeDesc; // 扫码关注文案
  public String searchPicUrl; // 搜索关注图片Url
  public String qrcodePicUrl; // 扫码关注图片Url
}
