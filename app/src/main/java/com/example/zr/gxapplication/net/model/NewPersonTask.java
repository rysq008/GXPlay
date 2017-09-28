package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by huchen on 16/9/23.
 */
public class NewPersonTask extends BaseModel implements Serializable {
  /**
   * state : 0-没有完成任何新手任务的新手 1-新手任务进行中 2-老司机
   * downloadCount : 紫金数量:返回数量代表完成度
   * agentCount : 特工数量
   * newDownloadAward : 新手紫金任务奖励（分）：目前是 30
   * newAgentAward : 新手特工任务奖励（分）: 目前是 0
   */

  public enum State {
    NEW(0),
    ING(1),
    OLD(2);

    private int code;

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    State(int code) {
      this.code = code;
    }
  }

  public enum DownLoadCount {
    ZERO(0),
    ONE(1),
    TWO(2);

    private int code;

    public long getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }


    DownLoadCount(int code) {
      this.code = code;
    }
  }

  public enum AgentCount {
    ZERO(0),
    ONE(1);

    private int code;

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    AgentCount(int code) {
      this.code = code;
    }
  }

  public int state;
  public int downloadCount;
  public int agentCount;
  public int newDownloadAward;
  public int newAgentAward;
  public int area;
}
