package com.game.helper.model.BaseModel;

import cn.droidlover.xdroidmvp.net.IModel;

/**
 * Created by zr on 2017-10-13.
 */
public class HttpResultModel<T extends IModel> extends XBaseModel {
    public T data;

    @Override
    public boolean isNull() {
        return data.isNull();
    }

    @Override
    public boolean isAuthError() {
        return code.equals("00006");//验证错误（）
    }

    @Override
    public boolean isBizError() {
        return super.isBizError();//业务错误
    }
}
