package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2017/12/22.
 */

public class GamePackageInfoResult extends XBaseModel {

    /**
     * game : {"class_type":{"id":1,"name":"网游"},"intro":"集结，被选召的孩子们，冒险再次起程！","logo":"/upload/image/20161210/20161210162000_239.jpg","download_count":0,"type":{"id":9,"name":"角色"},"id":47,"name":"数码大冒险"}
     * zhekou_xuchong : 6.6
     * filesize : 172.7
     * discount_vip : 4.4
     * zhekou_shouchong : 5.5
     * id : 22893
     * channel : {"id":15,"name":"当乐"}
     */

    private GameBean game;
    private double zhekou_xuchong;
    private double filesize;
    private double discount_vip;
    private double zhekou_shouchong;
    private int id;
    private ChannelBean channel;

    public GameBean getGame() {
        return game;
    }

    public void setGame(GameBean game) {
        this.game = game;
    }

    public double getZhekou_xuchong() {
        return zhekou_xuchong;
    }

    public void setZhekou_xuchong(double zhekou_xuchong) {
        this.zhekou_xuchong = zhekou_xuchong;
    }

    public double getFilesize() {
        return filesize;
    }

    public void setFilesize(double filesize) {
        this.filesize = filesize;
    }

    public double getDiscount_vip() {
        return discount_vip;
    }

    public void setDiscount_vip(double discount_vip) {
        this.discount_vip = discount_vip;
    }

    public double getZhekou_shouchong() {
        return zhekou_shouchong;
    }

    public void setZhekou_shouchong(double zhekou_shouchong) {
        this.zhekou_shouchong = zhekou_shouchong;
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

    public static class GameBean {
        /**
         * class_type : {"id":1,"name":"网游"}
         * intro : 集结，被选召的孩子们，冒险再次起程！
         * logo : /upload/image/20161210/20161210162000_239.jpg
         * download_count : 0
         * type : {"id":9,"name":"角色"}
         * id : 47
         * name : 数码大冒险
         */

        private ClassTypeBean class_type;
        private String intro;
        private String logo;
        private int download_count;
        private TypeBean type;
        private int id;
        private String name;

        public String getUrl() {
            return url ="https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;

        public ClassTypeBean getClass_type() {
            return class_type;
        }

        public void setClass_type(ClassTypeBean class_type) {
            this.class_type = class_type;
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

        public int getDownload_count() {
            return download_count;
        }

        public void setDownload_count(int download_count) {
            this.download_count = download_count;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class ClassTypeBean {
            /**
             * id : 1
             * name : 网游
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

        public static class TypeBean {
            /**
             * id : 9
             * name : 角色
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
         * id : 15
         * name : 当乐
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
