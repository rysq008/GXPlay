package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

/**
 * author: zhou date: 2016/1/20.
 */
//
public class AgentTaskCollect extends BaseModel implements Serializable {
  public long noSubmitTask;// : Long 正在进行中的任务
  public long checkSuccessTask;// : Long 审核成功的任务
  public long checkFailTask;// : Long 审核失败的任务
  public int newFailedTask;// 新审核失败任务个数
  public int newSuccessTask;// 新审核通过任务个数
}
