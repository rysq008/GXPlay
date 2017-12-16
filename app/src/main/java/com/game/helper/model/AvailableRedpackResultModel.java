package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

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

    public static class ListBean implements ItemType {


        private int red_id;
        private String amount;
        private String money_limit;
        private List<GameBean> games;
        private String name;
        private String end_date;
        private int my_red_id;
        private int type;
        private int kind;
        private boolean select;

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

            private String name;

        }

    }



}
