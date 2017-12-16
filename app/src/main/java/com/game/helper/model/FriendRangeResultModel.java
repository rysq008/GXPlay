package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class FriendRangeResultModel extends XBaseModel {


    private List<ListBean> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements ItemType {
        /**
         * member : {"nick_name":"这个昵称很好","icon":"/media/touxiang/4633079c6096782f654f9fa8cb0d9497.jpg","signature":"今天改了个昵称，很开心。","id":11,"vip_level":{"id":99,"level":0}}
         * total_promotion_number : 1
         * id : 5
         */

        public int pos;
        private MemberBean member;
        private int total_promotion_number;
        private String zongshouyi;
        private int id;

        public MemberBean getMember() {
            return member;
        }

        public void setMember(MemberBean member) {
            this.member = member;
        }

        public int getTotal_promotion_number() {
            return total_promotion_number;
        }

        public void setTotal_promotion_number(int total_promotion_number) {
            this.total_promotion_number = total_promotion_number;
        }

        public String getZongshouyi() {
            return zongshouyi;
        }

        public void setZongshouyi(String zongshouyi) {
            this.zongshouyi = zongshouyi;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class MemberBean {
            /**
             * nick_name : 这个昵称很好
             * icon : /media/touxiang/4633079c6096782f654f9fa8cb0d9497.jpg
             * signature : 今天改了个昵称，很开心。
             * id : 11
             * vip_level : {"id":99,"level":0}
             */

            private String nick_name;
            private String icon;
            private String signature;
            private int id;
            private VipLevelBean vip_level;

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public VipLevelBean getVip_level() {
                return vip_level;
            }

            public void setVip_level(VipLevelBean vip_level) {
                this.vip_level = vip_level;
            }

            public static class VipLevelBean {
                /**
                 * id : 99
                 * level : 0
                 */

                private int id;
                private int level;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }
            }
        }
    }

}
