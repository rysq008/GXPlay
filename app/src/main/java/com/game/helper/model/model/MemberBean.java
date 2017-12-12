package com.game.helper.model.model;

import com.game.helper.model.BaseModel.XBaseModel;

public class MemberBean extends XBaseModel {

        public String nick_name;
        public int id;
        public String icon;

        public MemberBean(String nick_name, int id, String icon) {
            this.nick_name = nick_name;
            this.id = id;
            this.icon = icon;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
}
