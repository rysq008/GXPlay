package com.zhny.library.presenter.login.model.vo;


import java.io.Serializable;

/**
 * 登陆vo
 *
 */

public class LoginVo implements Serializable {
    private String password;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
