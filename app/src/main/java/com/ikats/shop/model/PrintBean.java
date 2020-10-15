package com.ikats.shop.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrintBean implements Serializable, Cloneable {
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

    public List<GoodsBean> deepCopyList() {
        List<GoodsBean> dest = list;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(list);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<GoodsBean>) in.readObject();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return dest;
    }
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
