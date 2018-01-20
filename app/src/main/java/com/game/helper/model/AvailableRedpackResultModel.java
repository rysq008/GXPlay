package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class AvailableRedpackResultModel extends XBaseModel {

    public AvailableRedpackResultModel() {
    }

    private List<ListBean> list = new ArrayList<>();

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

    public static class ListBean implements ItemType, Serializable {


        private int red_id;//红包id
        private String amount;//红包金额
        private String money_limit;//启用红包金额
        private List<GameBean> games;//支持的游戏列表
        private String name;//红包名称
        private String end_date;
        private String use_rule;
        private int my_red_id;//我的红包id 为0表示无效的
        private int type;//1为通用红包 2指定游戏红包
        private int kind;//1为单发红包 2为群发红包
        private boolean select;

        @Override
        public boolean equals(Object obj) {
            AvailableRedpackResultModel.ListBean bean = (AvailableRedpackResultModel.ListBean) obj;
            return this.red_id == bean.getRed_id()
                    && this.amount.equals(bean.getAmount())
                    && this.money_limit.equals(bean.getMoney_limit())
                    && this.name.equals(bean.getName())
                    && this.end_date.equals(bean.getEnd_date())
                    && this.my_red_id == bean.getMy_red_id()
                    && this.type == bean.getType();
//                    && this.games.equals(bean.getGames());
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }


        public int getRed_id() {
            return red_id;
        }

        public void setRed_id(int red_id) {
            this.red_id = red_id;
        }

        public String getUse_rule() {
            return use_rule;
        }

        public void setUse_rule(String use_rule) {
            this.use_rule = use_rule;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMoney_limit() {
            return money_limit;
        }

        public void setMoney_limit(String money_limit) {
            this.money_limit = money_limit;
        }

        public List<GameBean> getGames() {
            return games;
        }

        public void setGames(List<GameBean> games) {
            this.games = games;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getMy_red_id() {
            return my_red_id;
        }

        public void setMy_red_id(int my_red_id) {
            this.my_red_id = my_red_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class GameBean {

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            private String name;

            private String logo;

            @Override
            public boolean equals(Object obj) {
                GameBean bean = (GameBean) obj;
                return this.name.equals(bean.getName())
                        && this.logo.equals(bean.getLogo());
            }
        }

    }


}
