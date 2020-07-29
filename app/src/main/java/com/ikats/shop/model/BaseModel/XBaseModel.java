package com.ikats.shop.model.BaseModel;


import com.ikats.shop.net.StateCode;

import java.io.Serializable;

import cn.droidlover.xdroidmvp.net.IModel;

public class XBaseModel implements IModel, ItemType, Serializable {
    protected boolean error;
    public String resultContent;
    public Integer resultCode;
    public int total_page;
    public int current_page;
    public long rettime;

    public boolean isNoneTradePassword() {
        return resultCode.equals(StateCode.STATE_0027) ? true : false;
    }

    public boolean isSucceful() {
        return StateCode.STATE_SUCCESS.equals(resultCode) ? true : false;
    }

    public boolean isPayStatus() {
        return resultCode.equals("0226") || resultCode.equals("0227") || resultCode.equals("0228") || resultCode.equals("0229")
                || resultCode.equals("0217") || resultCode.equals("0219");
    }

    public boolean hasNextPage() {
        return total_page > current_page;
    }

//    public String getResponseMsg() {
//        return StateCode.getMessage(code);
//    }

    public int nextPageNum() {
        return current_page++;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return false;
    }

    @Override
    public boolean isBizError() {
        return error;
    }

    @Override
    public String getErrorMsg() {
        return resultContent;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
