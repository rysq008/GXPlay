package com.zhny.library.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.amap.api.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    //添加谷歌图层
    public static TileOverlay addRemoteOverlay(AMap aMap){
        final String url = "http://mt0.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x=%d&y=%d&z=%d&s=";
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    return new URL(String.format(url, x, y, zoom));
                } catch (MalformedURLException e) {
                }
                return null;
            }
        };
        if (tileProvider != null) {
            return aMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider)
                    .diskCacheEnabled(true).diskCacheDir("/storage/emulated/0/amap/cache")
                    .diskCacheSize(100000)
                    .memoryCacheEnabled(true)
                    .memCacheSize(100000));
        }
        return null;
    }

    //获取一组坐标的中心
    public static LatLng getTheAreaCenter (List<LatLng> latLngList) {
        int total = latLngList.size();
        if (total == 0) return null;
        float X = 0, Y = 0, Z = 0;
        for (LatLng latLng : latLngList) {
            double lng = latLng.longitude * Math.PI / 180;
            double lat = latLng.latitude * Math.PI / 180;
            double x, y, z;
            x = Math.cos(lat) * Math.cos(lng);
            y = Math.cos(lat) * Math.sin(lng);
            z = Math.sin(lat);
            X += x;
            Y += y;
            Z += z;
        }
        X = X / total;
        Y = Y / total;
        Z = Z / total;

        double Lng = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);
        return new LatLng(Lat * 180 / Math.PI, Lng * 180 / Math.PI);
    }


    // 数据分组 解决绘制大量数据 地图变化过程中 线移动的问题
    public static List<List<LatLng>> groupList(List<LatLng> points, int length) {
        List<List<LatLng>> listGroup = new ArrayList<>();
        int listSize = points.size();
        int toIndex = length;
        for (int i = 0; i < points.size(); i += length) {
            if (i + length > listSize) {
                toIndex = listSize - i;
            }
            List<LatLng> newList = points.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }


}
