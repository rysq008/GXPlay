package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2018/1/4.
 */

public class EasemobAccountResults extends XBaseModel {

    /**
     * passwd : 341234G9123
     * user : 13312341234
     */

    private String passwd;
    private String user;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
