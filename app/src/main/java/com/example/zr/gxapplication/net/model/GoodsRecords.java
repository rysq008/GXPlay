package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/12/20.
 */
public class GoodsRecords implements Serializable {
  public String smallPic;
  public int status;
  public long joinTimes;
  public int goodsNo;
  public String goodsName;
  public boolean needAddress;
  public Long createTime;
  public String orderStatus;
  public String orderInfo;

  public enum GoodRecordsStatus {
    HAVE_IN_HAND("进行中", 0L),
    BEING_REVEALED("等待揭晓", 1L),
    ALREADY_ANNOUNCED("已揭晓", 2L),
    FAILED_TO_RAISE("集资失败", 3L),
    WINNER("恭喜中奖", 4L);

    private long code;
    private String desc;

    public long getCode() {
      return code;
    }

    public void setCode(long code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    GoodRecordsStatus(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static GoodRecordsStatus getVal(long code) {
      GoodRecordsStatus goodDetailStatus = null;
      for (GoodRecordsStatus gds : GoodRecordsStatus.values()) {
        if (gds.getCode() == (code)) {
          goodDetailStatus = gds;
          break;
        }
      }
      return goodDetailStatus;
    }
  }
}
