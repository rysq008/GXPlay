package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2016/12/26.
 */
public class ShareList implements Serializable {
  public long createTime;
  public long shareId;
  public String shareWords;
  public String userName;
  public String photo;
  public String goodsName;
  public List<String> smallImages;
  public List<String> shareImages;
}
