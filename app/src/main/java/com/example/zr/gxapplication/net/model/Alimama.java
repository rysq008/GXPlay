package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2017/3/7.
 */
public class Alimama implements Serializable {

  public Data data;

  public class Data implements Serializable {

    public List<PageList> pageList;

    public class PageList implements Serializable {
      public String pictUrl;
      public String title;
      public long auctionId;
      public double zkPrice;
      public double tkCommFee;
      public double tkRate;
    }
  }
}
