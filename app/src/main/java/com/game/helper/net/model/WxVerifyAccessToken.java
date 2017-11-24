package com.game.helper.net.model;

import java.io.Serializable;

/**
 * Created by wangsongyan on 15/12/4.
 * Email: wangsongyan@shandianshua.com
 */
public class WxVerifyAccessToken implements Serializable {

  public String errcode; // 0为有效,40003为无效
  public String errmsg; // ok为有效,invalid openid为无效

}
