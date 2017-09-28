package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

public class AppIncome extends BaseModel implements Serializable {

  public long nextStart;// : Long 下一页开始编号
  public long defaultCount;// Long 默认下一页数量
  public boolean hasNextPage;// : Boolean 是否有下一页
  public List<AppIncomeModel> list;

  public class AppIncomeModel implements Serializable {
    public long id;// : Long id
    public String nick;// String 用户昵称
    public String avatar;// String 用户头像
    public String image;// : String 图片地址
    public String desc;// : String 文本描述
    public long createdTime;// Long 创建时间
  }
}
