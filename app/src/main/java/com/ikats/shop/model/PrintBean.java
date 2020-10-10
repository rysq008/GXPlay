package com.ikats.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrintBean implements Serializable {
    public String shop_name;
    public String shop_code;
    public String sell_code;
    public String cashier;
    public final List<GoodsBean> list = new ArrayList<>();
    public String total;
    public String discounts;
    public String cope;
    public String paid;
    public boolean isselect;//辅助逻辑，用于判断是否选中状态

//    public class EncounterParticipantConverter implements PropertyConverter<List<GoodsBean>, String> {
//
//        @Override
//        public List<GoodsBean> convertToEntityProperty(String jsonString) {
//            if (jsonString == null) {
//                return null;
//            }
//
//            return new Gson().fromJson(jsonString, new TypeToken<List<GoodsBean>>() {
//            }.getType());
//        }
//
//        @Override
//        public String convertToDatabaseValue(List<GoodsBean> GoodsBeanList) {
//            return GoodsBeanList == null ? null : new Gson().toJson(GoodsBeanList);
//        }
//    }

}
