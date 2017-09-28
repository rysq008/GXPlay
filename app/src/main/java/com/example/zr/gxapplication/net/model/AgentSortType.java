package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;


public class AgentSortType extends BaseModel implements Serializable {

  public List<SortTypeBean> result;

  public class SortTypeBean implements Serializable {
    public Integer key;
    public String value;
  }
}
