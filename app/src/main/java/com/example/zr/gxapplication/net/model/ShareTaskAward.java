package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2016/11/3.
 */
public class ShareTaskAward extends BaseModelNew implements Serializable {
  public List<ShareTaskDetail> result;

  public class ShareTaskDetail implements Serializable {
    public String date;
    public String nickName;
    public int award;
    public int id;
  }
}
