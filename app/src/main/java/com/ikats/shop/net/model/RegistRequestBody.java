package com.ikats.shop.net.model;

public class RegistRequestBody extends BaseRequestBody {

    //
//    phone	是	string	电话
//    code	是	string	密码或者短信验证码
//    types	是	string	登录类型, 0:密码, 1:短信
//    channel_num	否	string	渠道编码 默认为空””
    public String phone;
    public String code;
    public String type;
    public String channel_num;

    public RegistRequestBody(String phone, String code, String type, String channel_num) {
        super(1);
        this.phone = phone;
        this.code = code;
        this.type = type;
        this.channel_num = channel_num;
    }
}
