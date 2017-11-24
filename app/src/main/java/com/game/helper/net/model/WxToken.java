package com.game.helper.net.model;

import java.io.Serializable;

/**
 * Created by wangsongyan on 15/12/2.
 * Email: wangsongyan@shandianshua.com
 */
public class WxToken implements Serializable {
  public String access_token; // 微信token
  public long expires_in; // 过期时间 7200
  public String refresh_token; // 刷新token
  public String openid; // 用户openid;
  public String scope; // 域
  public String unionid; // 用户unionid
}
