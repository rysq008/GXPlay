package com.zhny.library.presenter.fence.helper;

import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class FenceHelper {

    //获取围栏坐标数据
    public static List<LatLng> getFenceLatLngs(List<String> pointList) {
        List<LatLng> latLngs = new ArrayList<>();
        if (pointList == null) return latLngs;
        try {
            for (String pointStr : pointList) {
                String[] points = pointStr.split(";");
                for (String point : points) {
                    String[] arr = point.split(",");
                    LatLng latLng = new LatLng(Double.valueOf(arr[1]), Double.valueOf(arr[0]));
                    latLngs.add(latLng);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLngs;
    }

    //组合数据
    public static List<FenceMachine> getFenceMachines(Fence fence) {
        List<FenceMachine> machines = new ArrayList<>();
        if (fence.deviceList == null) return machines;
        for (FenceMachine machine : fence.deviceList) {
            machine.productBrandMeaning = TextUtils.isEmpty(machine.productBrandMeaning) ? "" : machine.productBrandMeaning;
            machine.productModel = TextUtils.isEmpty(machine.productModel) ? "" : machine.productModel;
            machine.brandAndModel = machine.productBrandMeaning + "-" + machine.productModel;
            machines.add(machine);
        }
        return machines;
    }


    /**
     * 转换数据
     */
    public static List<Object> convertData(List<FenceMachine> data) {
        List<FenceMachine> machines1 = new ArrayList<>();
        List<FenceMachine> machines2 = new ArrayList<>();
        for (FenceMachine machine : data) {
            machine.productBrandMeaning = TextUtils.isEmpty(machine.productBrandMeaning) ? "" : machine.productBrandMeaning;
            machine.productModel = TextUtils.isEmpty(machine.productModel) ? "" : machine.productModel;
            machine.brandAndModel = machine.productBrandMeaning + "-" + machine.productModel;
            machine.outedFence = !machine.internalFlag;
            machine.linkedFence = machine.associatedFlag;
            if (machine.outedFence) {
                machine.checkType = -1;
                machines2.add(machine);
            } else {
                machine.checkType = machine.selfAssociatedFlag ? 1 : 0;
                machines1.add(machine);
            }
        }
        List<Object> objects = new ArrayList<>();
        if (!machines1.isEmpty()) {
            objects.add("可添加农机");
            objects.addAll(machines1);
        }
        if (!machines2.isEmpty()) {
            objects.add("不可添加农机");
            objects.addAll(machines2);
        }
        return objects;
    }

    /**
     * 获取选中的机具
     */
    public static List<FenceMachine> getSelectMachines(List<Object> data) {
        List<FenceMachine> list = new ArrayList<>();
        for (Object o : data) {
            if (o instanceof FenceMachine) {
                FenceMachine machine = (FenceMachine) o;
                if (machine.checkType == 1) list.add(machine);
            }
        }
        return list;
    }

    //获取sn列表
    public static List<String> getDevicesSns(List<FenceMachine> fenceMachines) {
        List<String> data = new ArrayList<>();
        if (fenceMachines == null) return data;
        for (FenceMachine machine : fenceMachines) {
            data.add(machine.sn);
        }
        return data;
    }

    public static FenceMachine getFenceMachine(List<FenceMachine> fenceMachines, String sn) {
        for (FenceMachine machine : fenceMachines) {
            if (TextUtils.equals(machine.sn, sn)) {
                return machine;
            }
        }
        return null;
    }
}
