package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

public class AgentConfigEnum implements Serializable {

  public static final String DATA_KEY = "data";
  public static final String LOCATION_KEY = "location";
  public static final String MAP_DATA_KEY = "map_data";
  public static final String SHOW_MAP = "show_map";
  public static final String TASK_NO = "id";
  public static final String TASK_TIME_OUT = "task_time_out";
  public static final String TASK_CERT_ID = "task_cert_id";
  public static final String TASK_AGAIN_DO = "task_again_do";

  public enum AgentTaskListType {
    NO_AND_SUBMIT("进行中的列表", 1L),
    CHECK_SUCCEED("审核通过列表", 3L),
    CHECK_FAIL("审核拒绝列表", 4L);
    private long code;
    private String desc;

    AgentTaskListType(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static AgentTaskListType getVal(long code) {
      AgentTaskListType agentTaskListType = null;
      for (AgentTaskListType at : AgentTaskListType.values()) {
        if (at.getCode() == (code)) {
          agentTaskListType = at;
          break;
        }
      }
      return agentTaskListType;
    }

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
  }

  public enum AgentTaskStatus {
    NOT_GET("未领取", 0L),
    NOT_SUBMIT("未提交", 1L),
    SUBMIT_NOT_CHECK("未审核", 2L),
    CHECK_SUCCESS("审核成功", 3L),
    CHECK_FAIL("审核失败", 4L);
    private long code;
    private String desc;

    AgentTaskStatus(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static AgentTaskStatus getVal(long code) {
      AgentTaskStatus agentTaskStatus = null;
      for (AgentTaskStatus ats : AgentTaskStatus.values()) {
        if (ats.getCode() == (code)) {
          agentTaskStatus = ats;
          break;
        }
      }
      return agentTaskStatus;
    }

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
  }

  public enum AgentVoucherType {
    IMAGE("只包含图片", 0L),

    TEXT("只包含文本", 1L),

    TEXT_IMAGE("图片和文本", 2L);

    private long code;
    private String desc;

    AgentVoucherType(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static AgentVoucherType getVal(long code) {
      AgentVoucherType agentVoucherType = null;
      for (AgentVoucherType avt : AgentVoucherType.values()) {
        if (avt.getCode() == (code)) {
          agentVoucherType = avt;
          break;
        }
      }
      return agentVoucherType;
    }

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
  }

  public enum DevicdType {

    EMULATOR("emulator_device", 0L),

    MOBILE("mobile_phone_device", 1L);

    private long code;
    private String desc;

    DevicdType(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static DevicdType getVal(long code) {
      DevicdType devicdType = null;
      for (DevicdType dt : DevicdType.values()) {
        if (dt.getCode() == (code)) {
          devicdType = dt;
          break;
        }
      }
      return devicdType;
    }

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
  }

  public enum AgentLocationType {
    DISABLE_LOCATION("不需要定位", 0L),
    NEED_LOCATION("需要定位", 1L);

    private long code;
    private String desc;

    AgentLocationType(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static AgentLocationType getVal(long code) {
      AgentLocationType devicdType = null;
      for (AgentLocationType dt : AgentLocationType.values()) {
        if (dt.getCode() == (code)) {
          devicdType = dt;
          break;
        }
      }
      return devicdType;
    }

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
  }

  public enum AgentSubmitImageCompress {
    DON_COMPRESS("不需要压缩", 0L),
    NEED_COMPRESS("需要压缩", 1L);

    private long code;
    private String desc;

    AgentSubmitImageCompress(String desc, long code) {
      this.code = code;
      this.desc = desc;
    }

    public static AgentSubmitImageCompress getVal(long code) {
      AgentSubmitImageCompress devicdType = null;
      for (AgentSubmitImageCompress dt : AgentSubmitImageCompress.values()) {
        if (dt.getCode() == (code)) {
          devicdType = dt;
          break;
        }
      }
      return devicdType;
    }

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
  }
}
