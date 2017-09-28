package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/11/29.
 */
public class IncomeTotal implements Serializable {
  public Integer id; // 分类ID，ID不存在时表示没有明细
  public String title; // 分类名称
  public long amount; // 金额
  public String desc; // 说明
}
