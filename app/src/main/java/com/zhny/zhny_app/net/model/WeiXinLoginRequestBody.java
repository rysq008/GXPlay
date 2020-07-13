package com.zhny.zhny_app.net.model;

/**
 *
 * 时   间：2019/3/15
 * 简   述：<功能简述>
 */
public class WeiXinLoginRequestBody extends BaseRequestBody {

    public String appOpenId;
    public String unionId;

    public WeiXinLoginRequestBody(String appOpenId, String unionId) {
        super(1);
        this.appOpenId = appOpenId;
        this.unionId = unionId;
    }
}
