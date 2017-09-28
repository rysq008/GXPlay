package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 16/7/16.
 */
public class TaskExamplesImageDesc implements Serializable {

  public enum UpLoadType{
    ALL,
    TAKE_PHOTO,
    SELECT_PHOTO
  }

  public String path; // 图片路径
  public String desc; // 文本描述
  public String localPath; // 本地存储路径
  public int type; // 上传图片类型

}
