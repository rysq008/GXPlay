package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2016/12/26.
 */
public class RechargeList implements Serializable {
  public String title;
  public long time;
  public int type;
  public int amount;

  public enum RechargeType {
    PAY("支出", 1),
    INCOME("收入", 2);

    private int code;
    private String desc;

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    RechargeType(String desc, int code) {
      this.code = code;
      this.desc = desc;
    }

    public static RechargeType getVal(int code) {
      RechargeType rechargeType = null;
      for (RechargeType rt : RechargeType.values()) {
        if (rt.getCode() == (code)) {
          rechargeType = rt;
          break;
        }
      }
      return rechargeType;
    }
  }
}
