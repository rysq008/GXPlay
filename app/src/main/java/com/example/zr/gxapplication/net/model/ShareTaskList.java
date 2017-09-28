package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2016/11/2.
 */
public class ShareTaskList extends BaseModelNew implements Serializable {

  public List<ShareTask> result;

  public class ShareTask implements Serializable {
    public String taskTitle;
    public long taskId;
    public long shareId;
  }
}
