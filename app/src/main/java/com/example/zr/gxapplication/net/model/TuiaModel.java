package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhoulei date: 15/12/9.
 */
public class TuiaModel implements Serializable {
  public Integer doneTimes;// 今日已经做了多少次了
  public Integer dayMaxTimes;// 每日最多做的次数
  public String stringInStep2;// 打开页面后的URL必须包含的字符串
  public String stringInStep3;// 最终广告业面必须包含的字符串
  public String url;// 刮奖页面链接
  public Integer award;// 单次刮奖奖励（单位：分）
  public Boolean isUploadStep1;// 是否已经上传1
  public Boolean isUploadStep2;// 是否已经上传2
}
