package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2016/11/29.
 */
public class IncomeList implements Serializable {

  public Integer id;

  public List<Rows> rows;

  public class Rows implements Serializable {
    public int amount;
    public long date;
    public String desc;
  }
}
