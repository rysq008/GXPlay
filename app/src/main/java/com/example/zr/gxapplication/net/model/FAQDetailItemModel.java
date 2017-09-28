package com.example.zr.gxapplication.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * author: LYB date: 2016/8/20.
 * FAQ
 */
public class FAQDetailItemModel extends BaseModel {
  private String title;
  private String desc;

  public List<FAQItemModel> helps;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public List<FAQItemModel> getHelps() {
    return helps;
  }

  public void setHelps(List<FAQItemModel> helps) {
    this.helps = helps;
  }

  public class FAQItemModel implements Serializable {
    private String title;
    private String desc;
    private boolean itemExpand;

    public boolean isItemExpand() {
      return itemExpand;
    }

    public void setItemExpand(boolean itemExpand) {
      this.itemExpand = itemExpand;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }
}
