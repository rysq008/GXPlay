package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

public class BaseRequestBody implements Serializable {
  public long start;
  public long count;
  public Integer tagId;
  public boolean hide;

  public BaseRequestBody(long st, long ct, Integer tagId, boolean hide) {
    start = st;
    count = ct;
    this.tagId = tagId;
    this.hide = hide;
  }

  public BaseRequestBody(long st, long ct) {
    start = st;
    count = ct;
    tagId = null;
    hide = false;
  }

  public BaseRequestBody() {
    this(0, 10);
  }

  public BaseRequestBody setTagId(Integer tagId) {
    this.tagId = tagId;
    return this;
  }

  public BaseRequestBody setHide(boolean hide) {
    this.hide = hide;
    return this;
  }

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

}
