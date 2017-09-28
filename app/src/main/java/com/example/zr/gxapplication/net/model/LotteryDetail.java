package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/12/26.
 */
public class LotteryDetail implements Serializable {
  public long goodsNo;
  public String goodsName;
  public String cqsscTerm;
  public String cqsscCode;
  public long luckyCode;
  public Long[] lotteryCodeTime;
}
