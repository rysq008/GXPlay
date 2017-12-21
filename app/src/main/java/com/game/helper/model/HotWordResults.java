package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;

public class HotWordResults extends XBaseModel {

    public List<HotWordItem> list;

    public class HotWordItem extends XBaseModel {
        public int id;
        public String name;
    }

    @Override
    public int itemType() {
        return 0;
    }
}
