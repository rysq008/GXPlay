package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/12/9.
 */
public class Goods implements Serializable {
  public String smallPic;
  public Integer joinPrice;
  public Integer joinedTimes;
  public Integer joinTimes;
  public Integer goodsNo;
  public String goodsName;
  public Integer status;

  public enum GoodStatus {
    HAVE_IN_HAND,
    BEING_REVEALED,
    ALREADY_ANNOUNCED,
    FAILED_TO_RAISE,
    OFFLINE
  }
}
