package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 16/7/16.
 */
public class Component implements Serializable {

  public static final int TEXT = 1;
  public static final int PHOTO = 2;
  public static final int ALBUM = 3;
  public static final int TAKE_PHOTO = 4;
  public static final int LOCATION = 5;
  public static final int AUDIO = 6;
  public static final int RADIO = 7;
  public static final int CHECK = 8;

  public static int DON_NEED_OPTION_OTHERS = 0;
  public static int NEED_OPTION_OTHERS = 1;

  public int id;
  public String tips_text;
  public String tips_image;
  public String localPath; // 本地存储路径
  public String textEdit;
  public String locationLat;
  public String locationLon;
  public int type;
  public Integer compress;
  public String value;
  public long fileSize;
  public String fileMd5;
  public String[] options;
  public int options_other;

  public Component(int id, String value, String localPath) {
    this.id = id;
    this.value = value;
    this.localPath = localPath;
  }

  @Override
  public String toString() {
    return "Component{" +
        "id=" + id +
        ", tips_text='" + tips_text + '\'' +
        ", tips_image='" + tips_image + '\'' +
        ", localPath='" + localPath + '\'' +
        ", textEdit='" + textEdit + '\'' +
        ", locationLat='" + locationLat + '\'' +
        ", locationLon='" + locationLon + '\'' +
        ", type=" + type +
        ", compress=" + compress +
        ", value='" + value + '\'' +
        ", fileSize=" + fileSize +
        ", fileMd5=" + fileMd5 +
        '}';
  }
}
