package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/5/18.
 */
public class TuiaEventModel implements Serializable {
  public String status;

  public TuiaEventModel setStatus(String status) {
    this.status = status;
    return this;
  }
}
