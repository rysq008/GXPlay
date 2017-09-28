package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 2016/12/16.
 */
public class GoodDetail implements Serializable {
  public String smallPic; // 预览小图
  public int joinPrice; // 没人次多少夺宝币
  public long createTime; // 创建时间
  public int status; // 商品状态
  public int currentTerm; // 当前期数
  public int pid; //
  public int joinTimes; // 总参与次数
  public String shortDesc;
  public int lotteryCode;
  public int goodsNo;
  public int continuePeriod;
  public int amount;
  public int joinedTimes;
  public String goodsName;
  public List<String> bannerPic;
  public String detailPath;
  public long preAnnounceTime;
  public long announceTime;
  public long nextGoodsId;
  public long serverTime;

  public enum GoodDetailStatus {
    HAVE_IN_HAND("进行中", 0L),
    BEING_REVEALED("等待揭晓", 1L),
    ALREADY_ANNOUNCED("已揭晓", 2L),
    FAILED_TO_RAISE("集资失败", 3L);

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

    GoodDetailStatus(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static GoodDetailStatus getVal(long code) {
      GoodDetailStatus goodDetailStatus = null;
      for (GoodDetailStatus gds : GoodDetailStatus.values()) {
        if (gds.getCode() == (code)) {
          goodDetailStatus = gds;
          break;
        }
      }
      return goodDetailStatus;
    }
  }
}
