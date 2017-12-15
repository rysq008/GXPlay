package com.game.helper.model.BaseModel;

import com.game.helper.net.StateCode;

import java.io.Serializable;

import cn.droidlover.xdroidmvp.net.IModel;
import zlc.season.practicalrecyclerview.ItemType;

public class XBaseModel implements IModel, ItemType, Serializable {
    protected boolean error;
    public String message;
    public String code;
    public int total_page;
    public int current_page;

    public boolean isNoneTradePassword(){
        return code.equals(StateCode.STATE_0027) ? true : false;
    }

    public boolean isSucceful() {
        return code.equals(StateCode.STATE_0000) ? true : false;
    }

    public boolean hasNextPage() {
        return total_page > current_page;
    }

    public String getResponseMsg() {
        return StateCode.getMessage(code);
    }

    public int nextPageNum(){
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
        return message;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
