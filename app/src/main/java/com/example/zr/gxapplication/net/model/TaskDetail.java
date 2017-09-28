package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huchen on 16/7/14.
 */
public class TaskDetail extends BaseModel implements Serializable {
  public final int DEFAULT_METER_UNIT = 1000;

  public Integer status; // 当前用户与该任务的状态
  public int id; // 任务编号
  public String title; // 任务标题
  public long free; // 剩余任务个数
  public long expire_time; // 截至时间
  public boolean end; // 任务是否已经结束
  public long submit_time; // 剩余时间
  public long amount; // 特工单价（分））
  public String check_desc; // 最长审核时间
  public String desc; // 任务简介
  public List<TaskStep> steps;
  public long share_award; // 分享奖励显示金额
  public boolean can_receive; // 此任务是否可以领取［对于关联任务，有一个已领取，则其它的无法领取］
  public String share_desc; // 分享奖励描述 这个奖励的组成部分
  public boolean again;// Boolean 是否可以继续做此任务
  public long distance;
  public double latitude;// 维度
  public double longitude;// Double 经度
  public String extend_url;//大V分享链接

  public enum TaskStatus {
    AVAILABLE, // 可进行
    NO_SUBMIT, // 未提交
    CHECK_SUCCEED, // 审核通过
    CHECK_FAIL // 审核拒绝
  }
}
