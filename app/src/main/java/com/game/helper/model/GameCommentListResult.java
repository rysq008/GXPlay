package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/25.
 */

public class GameCommentListResult extends XBaseModel {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements ItemType {
        /**
         * member : {"nick_name":"609346","icon_thumb":"/media/touxiang/tab_3_pressed_thumb.png","icon":"/media/touxiang/tab_3_pressed.png"}
         * content : 哈哈
         * create_time : 2017-11-17 19:40:13
         */

        private MemberBean member;
        private String content;
        private String create_time;

        public MemberBean getMember() {
            return member;
        }

        public void setMember(MemberBean member) {
            this.member = member;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class MemberBean implements ItemType {
            /**
             * nick_name : 609346
             * icon_thumb : /media/touxiang/tab_3_pressed_thumb.png
             * icon : /media/touxiang/tab_3_pressed.png
             */

            private String nick_name;
            private String icon_thumb;
            private String icon;

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getIcon_thumb() {
                return icon_thumb;
            }

            public void setIcon_thumb(String icon_thumb) {
                this.icon_thumb = icon_thumb;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            @Override
            public int itemType() {
                return 0;
            }
        }
    }
}
