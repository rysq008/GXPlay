package com.ikats.shop.net.model;

public class LoginRequestBody extends BaseRequestBody {

    //
//    phone	是	string	电话
//    code	是	string	密码或者短信验证码
//    types	是	string	登录类型, 0:密码, 1:短信
//    channel_num	否	string	渠道编码 默认为空””
    public String loginName;
    public String password;
    public String username;

    public LoginRequestBody(String loginName, String password) {
        super(1);
        this.loginName = loginName;
        this.password = password;
        this.username = loginName;
    }

}
