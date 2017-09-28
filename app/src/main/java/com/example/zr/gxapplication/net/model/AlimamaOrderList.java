package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 2017/3/8.
 */
public class AlimamaOrderList implements Serializable {
  public long uid;
  public long id;
  public long tk_rate;
  public long price;
  public String goods_title;
  public String goods_id;
  public long create_time;
  public String maybe_refund_price;
  public int order_status;
  public long order_no;
  public long refund_time;

  public enum OrderStatus {
    PAY_SUCCESS("付款成功", 1),
    BALANCE_SUCCESS("结算成功", 2),
    WAIT_MONEY("等待返钱", 3),
    ALREADY_MONEY("已经返钱", 4),
    ORDER_FAIL("订单取消", 10);
    private int code;
    private String desc;

    public long getCode() {
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

    OrderStatus(String desc, int code) {
      this.code = code;
      this.desc = desc;
    }

    public static OrderStatus getVal(int code) {
      OrderStatus orderStatus = null;
      for (OrderStatus os : OrderStatus.values()) {
        if (os.getCode() == (code)) {
          orderStatus = os;
          break;
        }
      }
      return orderStatus;
    }
  }
}
