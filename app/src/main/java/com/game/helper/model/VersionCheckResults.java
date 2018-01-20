package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2018/1/13.
 */

public class VersionCheckResults extends XBaseModel {


    /**
     * version_code : 0
     * url : http://ghelper.h5h5h5.cn/upload/apk/G9Game.apk
     * version : 2.0.6
     * has_new : true
     * is_force_update : false
     * desc : 1.更新啦 2.很好玩
     */

    private int version_code;
    private String url;
    private String version;
    private boolean has_new;
    private boolean is_force_update;
    private String desc;

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isHas_new() {
        return has_new;
    }

    public void setHas_new(boolean has_new) {
        this.has_new = has_new;
    }

    public boolean isIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(boolean is_force_update) {
        this.is_force_update = is_force_update;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
