package com.game.helper.model.BaseModel;

import java.io.Serializable;

import cn.droidlover.xdroidmvp.net.IModel;
import zlc.season.practicalrecyclerview.ItemType;

public class XBaseModel implements IModel, ItemType, Serializable {
    protected boolean error;
    public String message;
    public String code;
    public int total_page;
    public int current_page;

    public boolean hasNextPage() {
        return total_page > current_page;
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
