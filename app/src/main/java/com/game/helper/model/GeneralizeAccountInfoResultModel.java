package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import zlc.season.practicalrecyclerview.ItemType;

public class GeneralizeAccountInfoResultModel extends XBaseModel implements ItemType{

        private String yue_history;
        private String yujizongshouyi;
        private String url;
        private int viprenshu;
        private String zongshouyi;
        private MemberBean member;

        private int total_promotion_number;
        private String num;
        private String dongjie;
        private String yue;
        private int id;

    public String getYue_history() {
        return yue_history;
    }

    public void setYue_history(String yue_history) {
        this.yue_history = yue_history;
    }

    public String getYujizongshouyi() {
        return yujizongshouyi;
    }

    public void setYujizongshouyi(String yujizongshouyi) {
        this.yujizongshouyi = yujizongshouyi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViprenshu() {
        return viprenshu;
    }

    public void setViprenshu(int viprenshu) {
        this.viprenshu = viprenshu;
    }

    public String getZongshouyi() {
        return zongshouyi;
    }

    public void setZongshouyi(String zongshouyi) {
        this.zongshouyi = zongshouyi;
    }

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDongjie() {
        return dongjie;
    }

    public void setDongjie(String dongjie) {
        this.dongjie = dongjie;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
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

            private String nick_name;
            private String icon;
            private int id;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }
        }

}
