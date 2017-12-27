package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/25.
 */

public class GameGiftListResult extends XBaseModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements ItemType {
        /**
         * gift_content : 123123123
         * game : {"logo":"/upload/images/gameinfo/2017110117343428767.png","id":1,"logothumb":""}
         * remain_num : 1
         * id : 1
         * giftname : 123123
         */

        private String gift_content;
        private GameBean game;
        private int remain_num;
        private int id;
        private String giftname;

        public String getGift_content() {
            return gift_content;
        }

        public void setGift_content(String gift_content) {
            this.gift_content = gift_content;
        }

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
        }

        public int getRemain_num() {
            return remain_num;
        }

        public void setRemain_num(int remain_num) {
            this.remain_num = remain_num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGiftname() {
            return giftname;
        }

        public void setGiftname(String giftname) {
            this.giftname = giftname;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class GameBean {
            /**
             * logo : /upload/images/gameinfo/2017110117343428767.png
             * id : 1
             * logothumb :
             */

            private String logo;
            private int id;
            private String logothumb;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLogothumb() {
                return logothumb;
            }

            public void setLogothumb(String logothumb) {
                this.logothumb = logothumb;
            }
        }
    }
}
