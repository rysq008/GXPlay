package com.example.zr.gxapplication.net.model.request;

import java.io.Serializable;

/**
 * Created by huchen on 2016/12/26.
 */
public class ShareListBody implements Serializable {
  public static final int DEFAULT_PAGE_SIZE = 10;

  private String token;
  private Long lastTime;
  private int pageSize;

  public ShareListBody(String token, Long lastTime, int pageSize) {
    this.token = token;
    this.lastTime = lastTime;
    this.pageSize = pageSize;
  }
}
