package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * 不关心结果的result／只需要判断code = 0000
 * */
public class NotConcernResults extends XBaseModel {

    @Override
    public int itemType() {
            return 0;
        }
}
