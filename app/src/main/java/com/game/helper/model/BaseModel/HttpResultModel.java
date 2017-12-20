package com.game.helper.model.BaseModel;

import com.game.helper.net.StateCode;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.IModel;

/**
 * Created by zr on 2017-10-13.
 */
public class HttpResultModel<T> extends XBaseModel {
    public T data;

    @Override
    public boolean isNull() {
        if (data instanceof IModel)
            return ((IModel) data).isNull();
        return Kits.Empty.check(data);
    }

    @Override
    public boolean isAuthError() {
        return code.equals("0006");//登陆错误（验证错误）
    }

    @Override
    public boolean isSucceful() {
        return code.equals(StateCode.STATE_0000);
    }

    @Override
    public boolean isBizError() {
        return super.isBizError();//业务错误
    }

    @Override
    public String getResponseMsg() {
        return StateCode.getMessage(code);
    }
}
