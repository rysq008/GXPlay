package com.game.helper.net.model;

import java.util.List;

/**
 * Created by Tian on 2017/12/21.
 */

public class SpecialDetailRequestBody extends BaseRequestBody {

    public int theme_id;
    public SpecialDetailRequestBody(int id,int page) {
        super(page);
        this.theme_id = id;


    }


}
