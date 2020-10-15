package com.ikats.shop.database;

import com.ikats.shop.model.BaseModel.XBaseModel;
import com.ikats.shop.model.GoodsBean;
import com.ikats.shop.model.PrintBean;
import com.ikats.shop.utils.EncounterParticipantConverter;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public final class PrintTableEntiry extends XBaseModel {

    @Id
    public long _id;
    public String shop_name;
    public String shop_code;
    public String sell_code;
    public String cashier;
    @Convert(converter = EncounterParticipantConverter.class, dbType = String.class)
    public List<GoodsBean> list = new ArrayList<>();
    public String total;
    public String discounts;
    public String cope;//应付
    public String paid;//实付
    public boolean is_pay;//是否支付
    @Transient
    public boolean isSelect;

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

    public static PrintTableEntiry builder(PrintBean printBean) {
        PrintTableEntiry printTableEntiry = new PrintTableEntiry();
        printTableEntiry.shop_name = printBean.shop_name;
        printTableEntiry.shop_code = printBean.shop_code;
        printTableEntiry.sell_code = printBean.sell_code;
        printTableEntiry.cashier = printBean.cashier;
        printTableEntiry.list = printBean.deepCopyList();
        printTableEntiry.total = printBean.total;
        printTableEntiry.discounts = printBean.discounts;
        printTableEntiry.cope = printBean.cope;
        printTableEntiry.paid = printBean.paid;
        return printTableEntiry;
    }
}
