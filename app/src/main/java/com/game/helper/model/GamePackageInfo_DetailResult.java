package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Tian on 2017/12/23.
 */

public class GamePackageInfo_DetailResult extends XBaseModel {

    /**
     * images : [{"img":"/upload/images/gameinfo/2017111617075425709.png"},{"img":"/upload/images/gameinfo/2017111617075341712.png"}]
     * detail : yoyoyo
     */

    private String detail;
    private List<ImagesBean> images;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements ItemType{
        /**
         * img : /upload/images/gameinfo/2017111617075425709.png
         */

        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public int itemType() {
            return 0;
        }
    }
}
