package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/9.
 */
public class ConfigModel implements Serializable {
  private static final int CODE_VERSION = 1;

  public long invited_award = 40l;
  public long scan_award = 10l;
  public long commision_percent = 10;
  public String share_title;
  public String share_content;
  public String[] withdraw_scope;
  public String background_image;

  public long deadLine;

  public int version = CODE_VERSION;

  public boolean isVersionLow() {
    return version < CODE_VERSION;
  }

}
