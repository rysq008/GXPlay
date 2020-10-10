package com.ikats.shop.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ikats.shop.model.GoodsBean;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.kit.Kits;
import io.objectbox.converter.PropertyConverter;

public class EncounterParticipantConverter implements PropertyConverter<List<GoodsBean>, String> {

    @Override
    public List<GoodsBean> convertToEntityProperty(String jsonString) {
        if (Kits.Empty.check(jsonString)) {
            return new ArrayList<>();
        }

        return new Gson().fromJson(jsonString, new TypeToken<List<GoodsBean>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<GoodsBean> GoodsBeanList) {
        return Kits.Empty.check(GoodsBeanList) ? "" : new Gson().toJson(GoodsBeanList);
    }
}
