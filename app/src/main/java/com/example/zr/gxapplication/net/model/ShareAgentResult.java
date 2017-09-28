package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2017/3/30.
 */
public class ShareAgentResult implements Serializable {

  public String id;
  public List<ResultList> list;

  public class ResultList implements Serializable {
    public long uid;
    public String nick;
    public String title;
    public long time;
    public int status;
  }
}
