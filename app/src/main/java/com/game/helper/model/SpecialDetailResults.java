package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/21.
 */

public class SpecialDetailResults extends XBaseModel {
    public List<ListBean> list;


    public static class ListBean implements ItemType {
        /**
         * game_package : {"zhekou_shouchong":3.2,"zhekou_xuchong":4.4,"filesize":111,"discount_vip":0}
         * name : 仙剑七仙传
         * logothumb : /media/game_logo/4633079c6096782f654f9fa8cb0d9497_rFYE0pZ.jpg
         * intro : 好玩的手机页游
         * logo : /upload/images/gameinfo/2017111617090393746.png
         * type : {"id":1,"name":"策略回合"}
         * id : 6
         */

        public GamePackageBean game_package;
        public String name;
        public String logothumb;
        public String intro;
        public String logo;
        public TypeBean type;
        public int id;

        @Override
        public int itemType() {
            return 0;
        }

        public static class GamePackageBean {
            /**
             * zhekou_shouchong : 3.2
             * zhekou_xuchong : 4.4
             * filesize : 111
             * discount_vip : 0
             */

            public double zhekou_shouchong;
            public double zhekou_xuchong;
            public double filesize;
            public double discount_vip;
            public double discount_activity;

        }

        public static class TypeBean {
            /**
             * id : 1
             * name : 策略回合
             */

            public int id;
            public String name;

        }
    }
}
