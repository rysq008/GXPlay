package com.zhny.library.presenter.machine.helper;

import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.presenter.machine.util.MachineUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;




public class MachineDataHelper {


    public static List<Object> getDataAfterHandle(List<MachineDto> resultList) {
        List<Object> dataList = new ArrayList<Object>();
        Map<String, List<MachineDto>> listMap = MachineUtil.groupListByShowTypeInList(resultList);
        if (listMap == null || listMap.size() == 0) return null;
        //获取所有key
        Set<String> keys = listMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            dataList.add(key);
            List<MachineDto> machineDtos = listMap.get(key);
            if (machineDtos != null && machineDtos.size() > 0) {
                dataList.addAll(machineDtos);
            }
        }

        return dataList;
    }
}


