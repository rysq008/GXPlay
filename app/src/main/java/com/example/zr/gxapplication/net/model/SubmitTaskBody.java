package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 16/7/22.
 */
public class SubmitTaskBody implements Serializable {

  public List<String> images;
  public String explain;
  public String location;

}
