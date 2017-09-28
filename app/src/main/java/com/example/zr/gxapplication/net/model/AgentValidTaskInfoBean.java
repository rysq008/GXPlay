package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

public class AgentValidTaskInfoBean implements Serializable {
  public final int STATE_NOT_SUBMIT = 1;
  public final int STATE_NOT_CHECK = 2;
  public final int STATE_NOT_CLAIM = 0;
  public final int DEFAULT_METER_UNIT = 1000;

  public Long amount;
  public Long id;
  public Long free;
  public String title;
  public Integer status;// 0-未认领 1-未提交,2-未审核
  public Long expire_time;
  public Boolean recommend;
  public Long check_time;
  public String recommend_name;
  public Boolean isIgnore;
  public Double latitude;// 维度
  public Double longitude;// Double 经度
  public Long distance;// 距离
  public Integer order;
  public String short_desc;

}
