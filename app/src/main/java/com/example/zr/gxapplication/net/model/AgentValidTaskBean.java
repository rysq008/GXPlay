package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * Created by zhangmou on 2016/7/18.
 */
public class AgentValidTaskBean extends BaseModel implements Serializable {

  public long taskNo;// : Long 任务编号
  public long certId; // long 任务凭证ID
  public String taskName;// : String 任务标题
  public long freeNum;// : Long 剩余任务个数
  public boolean isJoinTask;// : Boolean 是否正在进行中
  public long endTime;// : Long 截至时间
  public long expiredTime;// : Long 剩余时间
  public long amount;// : Long 任务单价(单位：分)
  public String desc;// : Long 描述信息
  public long status;// : Long 当前状态, 参考
  public long maxCheckTime; // 最长审核时间
  public long order;// 显示顺序
  public boolean recommend;// 是否为特别推荐任务
  public String recommendName;// 任务推荐人
  public boolean again;
  public boolean isIgnore;// 是否为已经忽略

}
