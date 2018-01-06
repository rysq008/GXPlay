package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Tian on 2017/12/22.
 */

public class GamePackageListResult extends XBaseModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean extends XBaseModel {
        /**
         * discount_vip : 4
         * game : {"logo":"/upload/image/20161209/20161209114359_333.jpg","type":{"id":17,"name":"卡牌"},"id":1,"logothumb":"","name":"航海王强者之路"}
         * zhekou_xuchong : 6
         * filesize : 258.67
         * zhekou_shouchong : 5
         * name_package : com.shanghailvbing.hhw.guopan
         * path : http://down2.guopan.cn/andl/dda.php?appid=101147&cid=3714&t=1508722296
         * id : 22874
         * channel : {"id":20,"name":"果盘"}
         */

        private float discount_vip;
        private GameBean game;
        private float zhekou_xuchong;
        private double filesize;
        private float zhekou_shouchong;
        private String name_package;
        private String path;
        private int id;
        private ChannelBean channel;
        public Disposable disposable;


        public float getDiscount_vip() {
            return discount_vip;
        }

        public void setDiscount_vip(float discount_vip) {
            this.discount_vip = discount_vip;
        }

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
        }

        public float getZhekou_xuchong() {
            return zhekou_xuchong;
        }

        public void setZhekou_xuchong(float zhekou_xuchong) {
            this.zhekou_xuchong = zhekou_xuchong;
        }

        public double getFilesize() {
            return filesize;
        }

        public void setFilesize(double filesize) {
            this.filesize = filesize;
        }

        public float getZhekou_shouchong() {
            return zhekou_shouchong;
        }

        public void setZhekou_shouchong(float zhekou_shouchong) {
            this.zhekou_shouchong = zhekou_shouchong;
        }

        public String getName_package() {
            return name_package;
        }

        public void setName_package(String name_package) {
            this.name_package = name_package;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ChannelBean getChannel() {
            return channel;
        }

        public void setChannel(ChannelBean channel) {
            this.channel = channel;
        }

        @Override
        public int itemType() {
            return 0;
        }

        public static class GameBean {
            /**
             * logo : /upload/image/20161209/20161209114359_333.jpg
             * type : {"id":17,"name":"卡牌"}
             * id : 1
             * logothumb :
             * name : 航海王强者之路
             */

            private String logo;
            private TypeBean type;
            private int id;
            private String logothumb;
            private String name;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public TypeBean getType() {
                return type;
            }

            public void setType(TypeBean type) {
                this.type = type;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public static class TypeBean {
                /**
                 * id : 17
                 * name : 卡牌
                 */

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

        public static class ChannelBean {
            /**
             * id : 20
             * name : 果盘
             */

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
