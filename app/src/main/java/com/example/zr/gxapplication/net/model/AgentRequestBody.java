package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

public class AgentRequestBody implements Serializable {

  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final String DEFAULT_PAGE_NUM = "0";
  private String id;// 页码的唯一标示
  private Integer pagesize;// 每页显示条数
  private Integer tag_id;// 分类ID，ID不存在时表示没有明细
  private Boolean hide;// 是否忽略
  private Double latitude;// 维度
  private Double longitude;// 经度

  public AgentRequestBody(String pagenum, Integer pagesize, Integer tagid, Boolean hide) {
    this.id = pagenum;
    this.pagesize = pagesize;
    this.tag_id = tagid;
    this.hide = hide;
    this.latitude = null;
    this.longitude = null;
  }

  public AgentRequestBody(String pageNum, Integer pagesize) {
    this.id = pageNum;
    this.pagesize = pagesize;
    this.tag_id = null;
    hide = false;
    this.latitude = null;
    this.longitude = null;
  }

  public AgentRequestBody() {
    this(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
  }

  public AgentRequestBody(Integer pagesize) {
    this(DEFAULT_PAGE_NUM, pagesize);
  }

  public AgentRequestBody(String pageNum) {
    this(pageNum, DEFAULT_PAGE_SIZE);
  }

  public AgentRequestBody setLatLng(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    return this;
  }

  public AgentRequestBody setPageNum(String pageNum) {
    this.id = pageNum;
    return this;
  }

  public AgentRequestBody setTagId(Integer tagId) {
    this.tag_id = tagId;
    return this;
  }

  public AgentRequestBody setHide(Boolean hide) {
    this.hide = hide;
    return this;
  }

}
