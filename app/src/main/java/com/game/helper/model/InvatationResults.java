package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class InvatationResults extends XBaseModel {
    /*
{
    "message": "成功",
    "code": "0000",
    "data": {
        "list": [
            {
                "member": {
                    "nick_name": "这个昵称很好",
                    "signature": "今天改了个昵称，很开心。",
                    "user": {
                        "date_joined": "2017-10-31 01:39:52"
                    },
                    "icon": "/media/touxiang/4633079c6096782f654f9fa8cb0d9497.jpg"
                }
            }
        ]
    },
    "total_page": 1,
    "current_page": 1
}
    */

    @SerializedName("list")
    public List<InvatationListItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class InvatationListItem implements ItemType {
        public Member member;

        public static class Member implements ItemType {
            /*{
                   {
                              "nick_name": "这个昵称很好",
                              "signature": "今天改了个昵称，很开心。",
                              "user": {
                                  "date_joined": "2017-10-31 01:39:52"
                              },
                              "icon": "/media/touxiang/4633079c6096782f654f9fa8cb0d9497.jpg"
                          }
                  }*/
            public String nick_name;
            public String signature;
            public User user;
            public Vip vip_level;
            public String icon;

            public static class User implements ItemType {
                public String date_joined;

                @Override
                public int itemType() {
                    return 0;
                }
            }

            public static class Vip implements ItemType {
                public int level;

                @Override
                public int itemType() {
                    return 0;
                }
            }
            @Override
            public int itemType() {
                return 0;
            }
        }

        @Override
        public int itemType() {
            return 0;
        }
    }

    @Override
    public int itemType() {
            return 0;
        }
}
