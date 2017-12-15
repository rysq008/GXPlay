package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class GameAccountResultModel extends XBaseModel{


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
         * game_account : 15201675725
         * is_successed : false
         * game_logo : /upload/image/20161209/20161209195320_406.jpg
         * create_time : 2016-10-23 03:23:49
         * game_id : 16
         * total_recharge : 0.00
         * id : 9
         * is_xc : true
         * game_channel_name : 乐嗨嗨
         * total_save : 0.00
         * game_name : 大话西游
         * is_vip : false
         * vip_level : 0
         * game_channel_id : 13
         */

        private String game_account;
        private boolean is_successed;
        private String game_logo;
        private String create_time;
        private int game_id;
        private String total_recharge;
        private int id;
        private boolean is_xc;
        private String game_channel_name;
        private String total_save;
        private String game_name;
        private boolean is_vip;
        private int vip_level;
        private int game_channel_id;


        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected = false;

        public String getGame_account() {
            return game_account;
        }

        public void setGame_account(String game_account) {
            this.game_account = game_account;
        }

        public boolean isIs_successed() {
            return is_successed;
        }

        public void setIs_successed(boolean is_successed) {
            this.is_successed = is_successed;
        }

        public String getGame_logo() {
            return game_logo;
        }

        public void setGame_logo(String game_logo) {
            this.game_logo = game_logo;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }

        public String getTotal_recharge() {
            return total_recharge;
        }

        public void setTotal_recharge(String total_recharge) {
            this.total_recharge = total_recharge;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIs_xc() {
            return is_xc;
        }

        public void setIs_xc(boolean is_xc) {
            this.is_xc = is_xc;
        }

        public String getGame_channel_name() {
            return game_channel_name;
        }

        public void setGame_channel_name(String game_channel_name) {
            this.game_channel_name = game_channel_name;
        }

        public String getTotal_save() {
            return total_save;
        }

        public void setTotal_save(String total_save) {
            this.total_save = total_save;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }

        public int getGame_channel_id() {
            return game_channel_id;
        }

        public void setGame_channel_id(int game_channel_id) {
            this.game_channel_id = game_channel_id;
        }

        @Override
        public int itemType() {
            return 0;
        }
    }


}
