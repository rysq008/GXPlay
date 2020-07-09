package com.zhny.library.presenter.fence.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.TileOverlay;
import com.blankj.utilcode.util.ConvertUtils;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityFenceBinding;
import com.zhny.library.presenter.fence.FenceConstant;
import com.zhny.library.presenter.fence.adapter.FenceAdapter;
import com.zhny.library.presenter.fence.base.FenceBaseActivity;
import com.zhny.library.presenter.fence.custom.FenceAddMachinePopWin;
import com.zhny.library.presenter.fence.custom.FenceDetailPopWin;
import com.zhny.library.presenter.fence.helper.FenceHelper;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.vo.FenceVo;
import com.zhny.library.presenter.fence.viewmodel.FenceViewModel;
import com.zhny.library.presenter.monitor.model.vo.MapPath;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FenceActivity extends FenceBaseActivity implements FenceAdapter.OnFenceItemClickListener,
        FenceDetailPopWin.OnFenceDetailPopWinListener, FenceAddMachinePopWin.OnFenceAddMachineListener,
        AMap.OnMapClickListener, AMap.OnCameraChangeListener {

    private ActivityFenceBinding binding;
    private FenceViewModel viewModel;
    private FenceDetailPopWin fenceDetailPopWin;
    private FenceAddMachinePopWin fenceAddMachinePopWin;
    private MapView mapView;
    private AMap aMap;
    private PolygonOptions polygonOptions;
    private MarkerOptions markerOptions;
    private FenceAdapter fenceAdapter;
    private List<Fence> fenceData = new ArrayList<>();
    private List<LatLng> latLngData = new ArrayList<>();
    private TileOverlay tileOverlay;
    private Fence selectFence;
    private List<String> devicesSns = new ArrayList<>();

    private Boolean isActualShowCenter;
    private static final int SHOW_PLOT_CENTER_ZOOM = 14;


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(FenceViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fence);
        binding.dlFence.addDrawerListener(this);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapClickListener(this);
        aMap.setOnCameraChangeListener(this);

        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(getString(R.string.fence_title));

        if(tileOverlay != null) tileOverlay.remove();
        tileOverlay = MapUtils.addRemoteOverlay(aMap);

        fenceDetailPopWin = new FenceDetailPopWin(this, this);
        fenceAddMachinePopWin = new FenceAddMachinePopWin(this, this);

        fenceAdapter = new FenceAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.layoutFenceSelectFenceView.rvFenceSelectFence.setLayoutManager(manager);
        binding.layoutFenceSelectFenceView.rvFenceSelectFence.setAdapter(fenceAdapter);

        polygonOptions = new PolygonOptions()
                .strokeWidth(DisplayUtils.dp2px(2))
                .strokeColor(Color.parseColor("#009688"))
                .fillColor(Color.parseColor("#30009688"));
        markerOptions = new MarkerOptions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestFenceData(true);
    }

    private void requestFenceData(boolean isMovePoints) {
        aMap.clear();
        //请求围栏数据
        viewModel.getFences().observe(this, baseDto -> {
            if(baseDto.getContent()==null){
                //无数据
                binding.setHasContent(false);
            }else{
            List<Fence> fences = baseDto.getContent().getContent();
            if (fences != null && fences.size() > 0){
                drawFence(fences, isMovePoints);
            } else {
                //无数据
                binding.setHasContent(false);
            }
            }
        });
    }

    private void drawFence(List<Fence> fences, boolean isMovePoints) {
        fenceData.clear();
        latLngData.clear();
        float zoom = aMap.getCameraPosition().zoom;
        for (Fence fence : fences) {
            if (fence.disabled) continue;
            //设置缩略图数据
            List<LatLng> latLngs = FenceHelper.getFenceLatLngs(fence.pointList);
            fence.mapPath = new MapPath(aMap.getProjection(), latLngs);
            //绘制地图多边形
            Polygon polygon = aMap.addPolygon(polygonOptions);
            polygon.setPoints(fence.mapPath.latLngs);
            polygon.setZIndex(999);
            fence.polygon = polygon;
            //设置设备数
            fence.deviceNum = fence.deviceSns == null ? 0 : fence.deviceSns.size();
            //绘制中心marker
            BitmapDescriptor plotMarkerIcon = BitmapDescriptorFactory.fromView(getPlotView(fence.name));
            Marker centerMarker = aMap.addMarker(markerOptions);
            centerMarker.setPosition(MapUtils.getTheAreaCenter(fence.mapPath.latLngs));
            centerMarker.setIcon(plotMarkerIcon);
            centerMarker.setVisible(zoom >= SHOW_PLOT_CENTER_ZOOM);
            fence.marker = centerMarker;

            latLngData.addAll(latLngs);
            fenceData.add(fence);
        }
        //设置有无数据
        binding.setHasContent(fenceData.size() > 0);

        fenceAdapter.refresh(fenceData);
        if (isMovePoints) {
            movePoints(latLngData, 40, 0);
        }
    }

    private View getPlotView(String fenceName) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_plot_marker, null);
        view.setText(fenceName);
        return view;
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        if (fenceAdapter != null && !fenceData.isEmpty()) {
            fenceAdapter.refresh(fenceData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMap();
        if (mapView != null) mapView.onDestroy();
        if (binding != null) {
            binding.dlFence.removeDrawerListener(this);
            binding.unbind();
        }
    }

    private void clearMap() {

    }

    @Override
    public void onBackPressed() {
        if (binding.dlFence.isDrawerOpen(GravityCompat.END)) {
            binding.dlFence.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected boolean isShowAdding() {
        return true;
    }

    @Override
    protected void onAddListener() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FenceConstant.IS_ADD, true);
        startActivity(DrawFenceActivity.class);
    }

    //放大地图
    public void onMapZoomIn(View view) {
        super.onMapZoomIn(aMap);
    }

    //缩小地图
    public void onMapZoomOut(View view) {
        super.onMapZoomOut(aMap);
    }

    //选择菜单
    public void doubleLeft(View view) {
        binding.dlFence.openDrawer(binding.layoutFenceSelectFenceView.layoutFenceSelectFenceRoot);
    }

    @Override
    public void onFenceItemClick(Fence fence) {
        if (binding.dlFence.isDrawerOpen(GravityCompat.END)) {
            binding.dlFence.closeDrawers();
        }
        //选中绘制多边形的颜色
        if (fence.polygon != null) {
            fence.polygon.setStrokeColor(Color.parseColor("#0000FF"));
        }

        movePoints(fence.mapPath.latLngs, 40, 310);
        getFenceDetails(fence);
    }

    //查询围栏详情数据
    private void getFenceDetails(Fence fence) {
        //显示页面
        if (!fenceDetailPopWin.isShowing()) {
            fenceDetailPopWin.show(binding.getRoot(), getWindow(), fence);
        }
        //查询并更新数据
        viewModel.getFenceDetails(fence.geofenceId).observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                return;
            }
            this.selectFence = baseDto.getContent();
            fence.fenceMachines = FenceHelper.getFenceMachines(selectFence);
            //显示添加界面
            this.devicesSns.clear();
            this.devicesSns.addAll(FenceHelper.getDevicesSns(fence.fenceMachines));
            fenceDetailPopWin.refreshData(fence.fenceMachines);
        });
    }


    //移动到聚合点
    private void movePoints(List<LatLng> latLngs, int dpPadding, float dpBottom) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            boundsBuilder.include(latLng);
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), DisplayUtils.dp2px(dpPadding));
        cameraUpdate.getCameraUpdateFactoryDelegate().paddingBottom = DisplayUtils.dp2px(dpBottom);
        aMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onFenceDetailPopWinAdd(Fence fence) {
        //点击围栏详情添加按钮
        Log.d(TAG, "onFenceDetailPopWinAdd: " + fence.name);
//        if (fenceDetailPopWin.isShowing()) {
//            //关闭详情界面
//            fenceDetailPopWin.dismiss();
//        }
        getFenceCanAddMachines(fence);
    }

    //查询围栏机具
    private void getFenceCanAddMachines(Fence fence) {
        //展示页面
        if (!fenceAddMachinePopWin.isShowing()) {
            fenceAddMachinePopWin.show(binding.getRoot(), getWindow());
        }
        //请求数据
        viewModel.getFenceCanAddDevices(fence.geofenceId, null).observe(this, baseDto -> {
            selectFence.fenceSelectMachines = baseDto.getContent();
            fenceAddMachinePopWin.refreshData(selectFence);
        });
    }


    @Override
    public void onFenceDetailPopWinEdit(Fence fence) {
        //点击围栏详情编辑按钮
        if (fenceDetailPopWin.isShowing()) {
            fenceDetailPopWin.dismiss();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(FenceConstant.FENCE_ID, fence.geofenceId);
        bundle.putString(FenceConstant.FENCE_NAME, fence.name);
        bundle.putBoolean(FenceConstant.IS_ADD, false);
        startActivity(EditFenceActivity.class, bundle);
    }

    @Override
    public void onMachineDeleteListener(FenceMachine dto) {
        //点击围栏详情删除按钮
        Log.d(TAG, "onMachineDeleteListener: " + dto.name);
        if (devicesSns != null && devicesSns.size() > 0) {
            updateFence(true, Collections.singletonList(dto.sn));
        }
    }

    @Override
    public void onAddMachineListener(List<FenceMachine> params, Fence fence) {
        //关闭添加界面
        fenceAddMachinePopWin.dismiss();
        if (params != null && params.size() > 0) {
            List<String> addDevicesSns = FenceHelper.getDevicesSns(params);
            updateFence(false, addDevicesSns);
        }
    }


    private void updateFence(boolean isDelete, List<String> sns) {
        if (selectFence == null) return;
        FenceVo fenceVo = new FenceVo(selectFence.objectVersionNumber, selectFence.geofenceId, selectFence.name);
        fenceVo.points = selectFence.points;

        if (isDelete) devicesSns.remove(sns.get(0));
        else devicesSns.addAll(sns);
        fenceVo.deviceSns = devicesSns;

        viewModel.updateFence(fenceVo).observe(this, baseDto -> {
            getFenceDetails(selectFence);
            if (baseDto.getContent() != null && baseDto.getContent()) {
                devicesSns.clear();
                Toast.makeText(this, getString(R.string.fence_operate_success), Toast.LENGTH_SHORT).show();
                //重新请求围栏列表（防止设备变化后列表没有变化）
                requestFenceData(false);
            } else {
                //回复sn集合
                if (isDelete) {
                    devicesSns.add(sns.get(0));
                } else {
                    for (String sn : sns) {
                        devicesSns.remove(sn);
                    }
                }
            }
        });
    }

    @Override
    public void onAddDismiss(Fence fence) {
        //取消选中
        if (fence != null) {
            Polygon polygon = fence.polygon;
            if (polygon != null) {
                polygon.setStrokeColor(Color.parseColor("#009688"));
            }
        }
        //恢复可视区域
        movePoints(latLngData, 40, 0);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (latLng != null) {
            for (Fence fence : fenceData) {
                Polygon polygon = fence.polygon;
                if (polygon != null && polygon.contains(latLng)) {
                    //选中某个围栏
                    onFenceItemClick(fence);
                    break;
                }
            }
        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //围栏中心marker显示
        Boolean isShowPlotCenter = cameraPosition.zoom >= SHOW_PLOT_CENTER_ZOOM;
        if (isShowPlotCenter != isActualShowCenter) {
            isActualShowCenter = isShowPlotCenter;
            Log.d(TAG, "onCameraChangeFinish: 围栏中心：" + cameraPosition.zoom + " , " + isActualShowCenter);
            showPlotCenter(isActualShowCenter);
        }
    }

    private void showPlotCenter(boolean isShowCenter) {
        if (fenceData.size() > 0) {
            for (Fence fence : fenceData) {
                if (fence.marker != null) fence.marker.setVisible(isShowCenter);
            }
        }
    }

}
