package com.game.helper.model;

import com.game.helper.data.RxConstant;
import com.game.helper.model.BaseModel.XBaseModel;

import java.util.Map;

public class GeneralizeResults extends XBaseModel {

    public String yue_history;// 0.00,
    public String yujizongshouyi;// 200.00,
    public String url;// http;////127.0.0.1;//8000/?market_id=2&market_num=10000&signature=081425df59351e65f96ea0ee1f6fe8b4,
    public int viprenshu;// 0,
    public String zongshouyi;// 0.00,
    public Map<String, Object> member;// { nick_name;// 这个昵称很好,id;// 1,icon;// },
    public int total_promotion_number;// 4,
    public String num;// 10000,
    public String dongjie;// 0.00,
    public String yue;// 0.00,
    public int id;// 2


    @Override
    public int itemType() {
        return RxConstant.GeneralizeModeType.Generalize_Balance_Account_Info_type;
    }
}
