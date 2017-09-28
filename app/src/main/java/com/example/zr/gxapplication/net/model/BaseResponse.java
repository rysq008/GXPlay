package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/11/21.
 */
public class BaseResponse<T> implements Serializable {
  public static final String SUCCESS_CODE = "200";

  public String code;
  public String message;

  public T result;
}
