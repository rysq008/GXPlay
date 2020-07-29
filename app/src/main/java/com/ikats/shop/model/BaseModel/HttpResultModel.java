package com.ikats.shop.model.BaseModel;

import com.ikats.shop.net.StateCode;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.IModel;

public class HttpResultModel<T> extends XBaseModel {
    public T resultData;

    @Override
    public boolean isNull() {
        if (isSucceful())
            return false;
        if (resultData instanceof IModel)
            return ((IModel) resultData).isNull();
        return Kits.Empty.check(resultData);
    }

    @Override
    public boolean isAuthError() {
        return resultCode.equals(StateCode.STATE_0006);//登录错误（验证错误）
    }

    @Override
    public boolean isSucceful() {
        return super.isSucceful();
    }

    @Override
    public boolean isBizError() {
        return super.isBizError();//业务错误
    }

//    @Override
//    public String getResponseMsg() {
//        return StateCode.getMessage(code);
//    }
}
