package com.game.helper.model;

import com.game.helper.model.BaseModel.XBaseModel;

import java.util.List;
import java.util.Map;

public class SearchListResults extends XBaseModel {

    public List<SearchListItem> list;

    public class SearchListItem extends XBaseModel {
        /**
         "game_package":{
         "zhekou_shouchong":0,
         "zhekou_xuchong":0,
         "filesize":213.9,
         "discount_vip":0
         },
         "name":"放开那三国2（乐游旧版）",
         "logothumb":"",
         "intro":"乐游旧版",
         "logo":"/upload/image/20161224/20161224110613_374.jpg",
         "type":{
         "id":17,
         "name":"卡牌"
         },
         "id":9
         */
        public Map<String, Float> game_package;
        public String name;
        public String logothumb;
        public String intro;
        public String logo;
        public Map<String, Object> type;
        public int id;
    }
}
