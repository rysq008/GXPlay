package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyb on 17/1/6.
 */
public class TaskStepDetail extends BaseModel implements Serializable {

  public List<Component> component;
  public String title;//
  public String desc;//
  public List<String> images;// [],
  public int skip;// 0(选填)
  public String url;//

}
