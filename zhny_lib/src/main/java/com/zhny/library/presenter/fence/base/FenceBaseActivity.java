package com.zhny.library.presenter.fence.base;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.presenter.fence.custom.pie.PieData;
import com.zhny.library.presenter.fence.model.vo.DrawFence;
import com.zhny.library.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * created by liming
 */
public abstract class FenceBaseActivity extends BaseActivity implements DrawerLayout.DrawerListener, AMap.OnMarkerDragListener {

    public static final String TAG = "FenceBaseActivity";

    @Override
    protected boolean isShowBacking() {
        return true;
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: " + marker.getId());
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: " + marker.getId());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: " + marker.getId());
    }

    //坐标转换
    public List<LatLng> getPointsFromMarkers(List<DrawFence> markerData) {
        List<LatLng> data = new ArrayList<>(markerData.size());
        for (DrawFence fence : markerData) {
            data.add(fence.marker.getPosition());
        }
        return data;
    }



    //放大地图
    protected void onMapZoomIn(AMap aMap) {
        CameraUpdate update = CameraUpdateFactory.zoomIn();
        aMap.animateCamera(update);
    }

    //缩小地图
    protected void onMapZoomOut(AMap aMap) {
        CameraUpdate update = CameraUpdateFactory.zoomOut();
        aMap.animateCamera(update);
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    // 菜单创建器
    public SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(FenceBaseActivity.this).setBackgroundColor(Color.RED)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(DisplayUtils.dp2px(70))
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };



    public List<PieData> getPieDate() {
        List<PieData> pieDatas = new ArrayList<>();
        PieData pieData1 = new PieData(1, "清空", 1, R.drawable.icon_empty);
        PieData pieData2 = new PieData(2, "绘制", 1, R.drawable.icon_orientation_edit);
        PieData pieData3 = new PieData(3, "撤回", 1, R.drawable.icon_recalled);
        pieDatas.add(pieData1);
        pieDatas.add(pieData2);
        pieDatas.add(pieData3);
        return pieDatas;
    }


}
