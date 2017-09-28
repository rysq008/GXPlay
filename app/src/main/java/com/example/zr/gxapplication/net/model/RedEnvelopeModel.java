package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by lyb on 2016/9/2.
 */
public class RedEnvelopeModel extends BaseModel implements Serializable {

  public long id;// Long 红包ID
  public String title;// String 标题
  public String desc;// String 描述
  public long amount; // Long 金额，单位：分
  public String url;// String 红包链接
  public long startTime;// Long 开始时间
  public long endTime;// Long 过期时间
  public boolean used;// Boolean true-已用 false-未用
  public boolean expire;// Boolean true-过期 false-未过期

}
