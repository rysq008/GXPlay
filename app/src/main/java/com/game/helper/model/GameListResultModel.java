package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

public class GameListResultModel extends XBaseModel {


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

        private GamePackage game_package;
        private String name;
        private String logothumb;
        private String intro;
        private String logo;
        private int id;

        private TypeBean type;

        public GamePackage getGame_package() {
            return game_package;
        }

        public void setGame_package(GamePackage game_package) {
            this.game_package = game_package;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogothumb() {
            return logothumb;
        }

        public void setLogothumb(String logothumb) {
            this.logothumb = logothumb;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

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

        public TypeBean getType() {
            return type;
        }

        public void setType(TypeBean type) {
            this.type = type;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class GamePackage {

            private String zhekou_shouchong;
            private String zhekou_xuchong;
            private String filesize;
            private String discount_vip;

            public String getZhekou_shouchong() {
                return zhekou_shouchong;
            }

            public void setZhekou_shouchong(String zhekou_shouchong) {
                this.zhekou_shouchong = zhekou_shouchong;
            }

            public String getZhekou_xuchong() {
                return zhekou_xuchong;
            }

            public void setZhekou_xuchong(String zhekou_xuchong) {
                this.zhekou_xuchong = zhekou_xuchong;
            }

            public String getFilesize() {
                return filesize;
            }

            public void setFilesize(String filesize) {
                this.filesize = filesize;
            }

            public String getDiscount_vip() {
                return discount_vip;
            }

            public void setDiscount_vip(String discount_vip) {
                this.discount_vip = discount_vip;
            }
        }


        public static class TypeBean {

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

}
