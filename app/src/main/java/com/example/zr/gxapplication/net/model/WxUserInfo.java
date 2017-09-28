package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by wangsongyan on 15/12/4.
 * Email: wangsongyan@shandianshua.com
 */
public class WxUserInfo implements Serializable {
  public String openid;
  public String nickname;
  public int sex; // 1为男性，2为女性
  public String province;
  public String city;
  public String country;
  public String headimgurl;
  public String unionid;
}
