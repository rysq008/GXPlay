package com.zhny.library.presenter.machine.util;

import android.text.TextUtils;

import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.presenter.work.dto.WorkDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MachineUtil {

    public static Map<String, List<MachineDto>> groupListByShowTypeInList(List<MachineDto> machineDtos) {
        if (machineDtos == null || machineDtos.size() == 0) return null;
        Map<String, List<MachineDto>> map = new LinkedHashMap<>();

        for (MachineDto machineDto : machineDtos) {
            List<MachineDto> tmpList = map.get(machineDto.showTypeInList);
            if (tmpList == null) {
                if (!TextUtils.isEmpty(machineDto.showTypeInList)) {
                    tmpList = new ArrayList<>();
                    tmpList.add(machineDto);
                    map.put(machineDto.showTypeInList, tmpList);
                }
            } else {
                tmpList.add(machineDto);
            }
        }

        return map;
    }


    public static String queryValueFromList(String propertyName,
                                            List<MachineDetailsDto.PropertyListBean> propertyListBeans) {
        String value = "";
        if (propertyListBeans == null || propertyListBeans.size() == 0) return value;
        for (MachineDetailsDto.PropertyListBean p : propertyListBeans) {
            if (p.name.contains(propertyName)) {
                value = p.value;
                return value;
            }
        }
        return value;
    }

    //转化为标准时间格式
    public static String getStandardDate(String time) {
        if (TextUtils.isEmpty(time) || time.length() < 10) return "";
        return time.substring(0, 10);
    }


    /**
     * @param time 从接口得到的时间 2020-02-18 10:55:20
     * @return 年月日2020.02.18
     */
    public static String getYMD(String time) {
        if (TextUtils.isEmpty(time) || time.length() < 10) return "";

        String result = time.substring(0, 10);
        result = result.replace("-", ".");
        return result;


    }

    public static List<MachineDto> sortMachineByProductCategory(List<MachineDto> machineDtos) {
        if (machineDtos == null || machineDtos.size() == 0) {
            return null;
        }
        Collections.sort(machineDtos, new Comparator<MachineDto>() {
            @Override
            public int compare(MachineDto o1, MachineDto o2) {
                //升序
                return o1.productCategory.compareTo(o2.productCategory);
            }
        });
        return machineDtos;
    }


}
