package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by wangsongyan on 15/12/4.
 * Email: wangsongyan@shandianshua.com
 */
public class WxRefreshToken implements Serializable {

  public String access_token; // access_token
  public long expires_in; // 过期时间 7200
  public String refresh_token; // refresh_token
  public String openid; // openid
  public String scope; // 域名
}
