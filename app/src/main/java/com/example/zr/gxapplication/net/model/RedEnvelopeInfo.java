package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;


public class RedEnvelopeInfo extends BaseModel implements Serializable {

  public long nextStart;// : Long 下一页开始编号
  public long defaultCount;// Long 默认下一页数量
  public boolean hasNextPage;// : Boolean 是否有下一页
  public List<RedEnvelopeModel> list;

}
