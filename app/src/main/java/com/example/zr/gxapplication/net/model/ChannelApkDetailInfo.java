package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangsongyan on 16/1/23.
 * Email: wangsongyan@shandianshua.com
 */
public class ChannelApkDetailInfo implements Serializable {
  public String packageName;
  public String title;
  public long likesCount;
  public long installedCount;
  public long downloadCount;
  public String tagline;
  public String description;
  public Icons icons;
  public Screenshots screenshots;
  public List<ApkSize> apks;
}
