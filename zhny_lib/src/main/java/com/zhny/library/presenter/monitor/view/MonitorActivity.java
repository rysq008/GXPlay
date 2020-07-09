package com.zhny.library.presenter.monitor.view;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.TileOverlay;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityMonitorBinding;
import com.zhny.library.presenter.alarm.view.AlarmActivity;
import com.zhny.library.presenter.monitor.adapter.PlotExpandAdapter;
import com.zhny.library.presenter.monitor.adapter.SelectMachineAdapter;
import com.zhny.library.presenter.monitor.custom.GestureLayout;
import com.zhny.library.presenter.monitor.custom.cluster.Cluster;
import com.zhny.library.presenter.monitor.custom.cluster.ClusterClickListener;
import com.zhny.library.presenter.monitor.custom.cluster.ClusterItem;
import com.zhny.library.presenter.monitor.custom.cluster.ClusterOverlay;
import com.zhny.library.presenter.monitor.custom.cluster.ClusterRender;
import com.zhny.library.presenter.monitor.custom.cluster.RegionItem;
import com.zhny.library.presenter.monitor.custom.expand.WrapperExpandableListAdapter;
import com.zhny.library.presenter.monitor.helper.MonitorHelper;
import com.zhny.library.presenter.monitor.listener.SelectMachineListener;
import com.zhny.library.presenter.monitor.model.dto.ExpandData;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;
import com.zhny.library.presenter.monitor.model.vo.MapPath;
import com.zhny.library.presenter.monitor.viewmodel.MonitorViewModel;
import com.zhny.library.presenter.operate.view.OperatorActivity;
import com.zhny.library.presenter.playback.view.PlayBackMonthDetailActivity;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MonitorActivity extends BaseActivity implements GestureLayout.OnUpOrDownScrollListener, ClusterRender,
        AMap.OnMapClickListener, /* SelectMachineListener, SelectPlotListener,*/ AMapLocationListener,
        ClusterClickListener, ExpandableListView.OnChildClickListener, SelectMachineListener,
        AMap.OnMultiPointClickListener {

    private static final String TAG = "MonitorActivity";
    private static final int ANIMATE_BOUND = 150;
    private static final int REQUEST_DEVICE_PERIOD_SECOND = 30;

    private ActivityMonitorBinding binding;
    private MonitorViewModel viewModel;
    private boolean isShowBaseData;
    private boolean isShowBaseDataDetail;
    private ObjectAnimator animatorClBaseData;
    private SelectMachineAdapter selectMachineAdapter;
    //    private SelectFarmAdapter selectFarmAdapter;
    private PlotExpandAdapter plotExpandAdapter;


    private MapView mapView;
    private AMap aMap;
    private AMapLocationClient locationClient;
    private Marker locationMarker;
    private BitmapDescriptor locationBitmapDescriptor, plotMarkerIcon, plotMultiBitmapDescriptor;
    private BitmapDescriptor descriptorOnLineSelect, descriptorOnLine, descriptorOffLineSelect, descriptorOffLine;
    private List<SelectMachineDto> machineData = new ArrayList<>();
    private List<ClusterItem> clusterItems = new ArrayList<>();
    private List<LatLng> deviceLatLngs = new ArrayList<>();
    private List<SelectFarmDto.SelectPlotDto> plotDtoList = new ArrayList<>();
    private SelectMachineDto selectMachine;
    private PolygonOptions polygonOptions;
    private MarkerOptions markerOptions, locationMarkerOptions;
    private TileOverlay tileOverlay;
    private ClusterOverlay clusterOverlay;

    private ActiveBroadcast activeBroadcast;

    private Disposable disposable = Disposables.empty();
    private boolean isRunning;
    private long requestCount = 0;

    private Geocoder geocoder;

    private Boolean isActualShowCenter;
    private static final float SHOW_PLOT_CENTER_ZOOM = 14f;

    private MultiPointOverlayOptions plotMultiPointOptions;
    private MultiPointOverlay plotMultiPointOverlay;
    private List<MultiPointItem> plotPointItems = new ArrayList<>();

    private boolean isStop = false;


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monitor);
        mapView = binding.monitorMapView;
        mapView.onCreate(savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        tileOverlay = MapUtils.addRemoteOverlay(aMap);
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);

        aMap.setOnMapClickListener(this);
        aMap.setOnMultiPointClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void initBusiness() {

        viewModel.setParams(this);
        setToolBarTitle(getString(R.string.monitor_title));

        activeBroadcast = new ActiveBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Server.USER_NOT_ACTIVE);
        registerReceiver(activeBroadcast, intentFilter);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.glMonitorBaseData.glMonitorBaseDataRoot.setOnUpOrDownScrollListener(this);

        initAnim();
        initAdapter();
        initMapClient();

        geocoder = new Geocoder(this, Locale.SIMPLIFIED_CHINESE);
    }

    //选中地块
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        List<ExpandData> data = plotExpandAdapter.getData();
        if (data != null && data.get(groupPosition) != null && data.get(groupPosition).content != null
                && data.get(groupPosition).content.get(childPosition) != null) {
            ExpandData expandData = data.get(groupPosition);
            Object o = expandData.content.get(childPosition);
            if (expandData.dataType == PlotExpandAdapter.PLOT_DATA) {
                SelectFarmDto.SelectPlotDto plotDto = (SelectFarmDto.SelectPlotDto) o;
                onPlotSelected(plotDto);
            } else if (expandData.dataType == PlotExpandAdapter.DEVICE_DATA) {
                //此处暂时无用
                SelectMachineDto machineDto = (SelectMachineDto) o;
                onMachineSelected(machineDto);
            }
        }
        return false;
    }

    @Override
    public boolean onPointClick(MultiPointItem multiPointItem) {
        LatLng multiLatLng = multiPointItem.getLatLng();
        for (SelectFarmDto.SelectPlotDto dto : plotDtoList) {
            if (dto.plotMarker != null) {
                LatLng position = dto.plotMarker.getPosition();
                if (position.latitude == multiLatLng.latitude && position.longitude == multiLatLng.longitude) {
                    onPlotSelected(dto);
                    break;
                }
            }
        }
        return true;
    }

    //用户未激活弹出dialog
    class ActiveBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), Constant.Server.USER_NOT_ACTIVE)) {
                startActivity(ActiveActivity.class);
                finish();
            }
        }
    }


    private void initMapClient() {
        aMap.getUiSettings().setZoomControlsEnabled(false);

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setOnceLocation(true); //设置单次定位
        // option.setInterval(2000); //连续定位
        option.setLocationCacheEnable(false); //关闭缓存机制，若连续定位可设置为true
        locationClient.setLocationOption(option);

        polygonOptions = new PolygonOptions()
                .strokeWidth(DisplayUtils.dp2px(1f))
                .strokeColor(Color.parseColor("#FFB100"))
                .fillColor(Color.parseColor("#00000000")).zIndex(995);

        markerOptions = new MarkerOptions();

        locationBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.lib_icon_monitor_location);
        locationMarkerOptions = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(locationBitmapDescriptor).zIndex(999);

        descriptorOnLineSelect = getDescriptorView(true, true);
        descriptorOnLine = getDescriptorView(false, true);
        descriptorOffLineSelect = getDescriptorView(true, false);
        descriptorOffLine = getDescriptorView(false, false);

        //初始化聚合
        clusterOverlay = new ClusterOverlay(aMap, getApplicationContext());
        clusterOverlay.setOnClusterClickListener(this);
        clusterOverlay.setClusterRender(this);

        plotMultiBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_multi_point_plot);
        plotMultiPointOptions = new MultiPointOverlayOptions().icon(plotMultiBitmapDescriptor);
    }


    private void initAdapter() {
        selectMachineAdapter = new SelectMachineAdapter(this);
        LinearLayoutManager managerMachine = new LinearLayoutManager(this);
        managerMachine.setOrientation(LinearLayoutManager.VERTICAL);
        binding.llMonitorSelectView.rvMonitorSelectMachine.setLayoutManager(managerMachine);
        binding.llMonitorSelectView.rvMonitorSelectMachine.setAdapter(selectMachineAdapter);
//
//        selectFarmAdapter = new SelectFarmAdapter(this, this);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.llMonitorSelectView.rvMonitorSelectFarm.setLayoutManager(manager);
//        binding.llMonitorSelectView.rvMonitorSelectFarm.setAdapter(selectFarmAdapter);


        plotExpandAdapter = new PlotExpandAdapter();
        final WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(plotExpandAdapter);
        binding.llMonitorSelectView.expandableListView.setAdapter(wrapperAdapter);
        binding.llMonitorSelectView.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> false);
        binding.llMonitorSelectView.expandableListView.setOnChildClickListener(this);
    }

    private void initAnim() {
        animatorClBaseData = ObjectAnimator.ofFloat(binding.glMonitorBaseData.glMonitorBaseDataRoot,
                "translationY", 0f, -560f).setDuration(500);
    }


    @Override
    protected void onStart() {
        super.onStart();
        isStop = false;
        viewModel.setShowOrHideLocationView(true);

        //对接壳子测试 获取用户名
        if (Constant.IS_LIB_TOKEN) {
            String loginUserName = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.SP.LOGIN_USERNAME);
            Log.d(TAG, "onStart: 获取到map跳转用户名：" + loginUserName);
            if (TextUtils.isEmpty(loginUserName)) {
                Toast.makeText(this, getString(R.string.get_account_fail), Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> params = new HashMap<>(4);
            params.put("grant_type", Constant.Server.GRANT_TYPE);
            params.put("client_id", Constant.Server.CLIENT_MAP_ID);
            params.put("client_secret", Constant.Server.CLIENT_MAP_SECRET);
            params.put("username", loginUserName);

            //获取token
            viewModel.getToken(params).observe(this, tokenInfoDto -> {
                if (tokenInfoDto != null && !TextUtils.isEmpty(tokenInfoDto.getAccess_token())) {
                    SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.TOKEN, tokenInfoDto.getAccess_token());
                    //获取数据
                    getUser();
                } else {
                    Toast.makeText(this, getString(R.string.oauth_fail), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //自己测试 获取数据
            getUser();
        }

    }

    private void getUser() {
        viewModel.getUserInfo(this).observe(this, userInfoDto -> {
            if (userInfoDto != null) {
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.USER_ID, userInfoDto.getId());
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.USERNAME, userInfoDto.getName());
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.ORGANIZATION_ID, userInfoDto.getOrganizationId());

                //请求快码
                viewModel.queryFastCode(MonitorActivity.this);
                //请求页面数据
                getViewData();

            } else {
                Toast.makeText(this, "获取用户失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //获取界面数据
    private void getViewData() {
       //请求地块
        if (plotDtoList.size() == 0) {
            getPlotData();
        }
        //定时请求设备
        if (machineData.size() == 0) {
            isRunning = true;
            requestCount = 0;
            disposable = observable.subscribe();
        }
    }


    //请求地块数据
    private void getPlotData() {
        viewModel.getPlotData().observe(this, baseDto -> {
            if (baseDto.getContent() != null) {
                drawPlots(baseDto.getContent());
            }
        });
    }


    //定时获取设备数据
    private Observable observable = Observable.interval(0,REQUEST_DEVICE_PERIOD_SECOND, TimeUnit.SECONDS)
            .takeWhile(new Predicate<Long>() {
                @Override
                public boolean test(Long aLong) throws Exception {
                    return isRunning;
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doAfterNext(aLong -> {
                Log.d(TAG, "==定时器==requestCount：" + requestCount);
                if (!isStop) {
                    requestCount++;
                    getDevicesData();
                    Log.d(TAG, ":----->>> requestCount : " + requestCount);
                }
            });


    //请求设备列表
    private void getDevicesData() {
        viewModel.getDevices().observe(this, baseDto -> {
//            boolean isSelected;
            if (baseDto.getContent() != null) {
//                isSelected = true;
                machineData.clear();
                machineData.addAll(baseDto.getContent());
                addMarkersAndUpdateView();
                if (selectMachine != null) {
                    //请求农机实时数据
                    String today = TimeUtils.getTodayStr(new Date());
                    viewModel.getDevicesPropers(MonitorActivity.this, selectMachine.sn, today);
                    //修改实时监控的按钮
//                    viewModel.setMonitoringButton(selectMachine.status);
                }
            }
//            else {
//                isSelected = false;
//            }
//            viewModel.setShowOrHideMachine(isSelected && requestCount == 1);
//            binding.llMonitorSelectView.tvShowOrCloseMachine.setSelected(isSelected && requestCount == 1);
        });
    }


    //绘制所有地块并移动至中心
    private void drawPlots(List<SelectFarmDto> farms) {
        float zoom = aMap.getCameraPosition().zoom;
        boolean isShowPlot = zoom >= SHOW_PLOT_CENTER_ZOOM;
        List<ExpandData> expandDataList = new ArrayList<>();

        Single.just(farms).doOnSubscribe(disposable -> {
            clearPlots();
            plotMultiPointOverlay = aMap.addMultiPointOverlay(plotMultiPointOptions);

            for (SelectFarmDto farm : farms) {
                List<Object> plots = new ArrayList<>();
                for (SelectFarmDto.SelectPlotDto plotDto : farm.fieldList) {
                    //① 地块绘制
                    plotDto.latLngs = MonitorHelper.getLatLngs(plotDto.coordinates);
                    Polygon polygon = aMap.addPolygon(polygonOptions);
                    polygon.setVisible(isShowPlot);
                    polygon.setPoints(plotDto.latLngs);
                    plotDto.polygon = polygon;
                    //② 中心点绘制
                    if (!TextUtils.isEmpty(plotDto.center)) {
                        String[] str = plotDto.center.split(",");
                        LatLng centerLatLng = new LatLng(Double.valueOf(str[1]), Double.valueOf(str[0]));
                        plotMarkerIcon = BitmapDescriptorFactory.fromView(getPlotView(plotDto.fieldName));
                        Marker plotCenterMarker = aMap.addMarker(markerOptions);
                        plotCenterMarker.setIcon(plotMarkerIcon);
                        plotCenterMarker.setPosition(centerLatLng);
                        plotCenterMarker.setVisible(isShowPlot);
                        plotDto.plotMarker = plotCenterMarker;

                        plotPointItems.add(new MultiPointItem(centerLatLng));
                    }
                    //③ map path
                    plotDto.mapPath = new MapPath(binding.mapView.getMap().getProjection(), plotDto.latLngs);

                    plots.add(plotDto);//记录每个牧场的地块
                    plotDtoList.add(plotDto); //记录全部地块
                }
                expandDataList.add(new ExpandData(PlotExpandAdapter.PLOT_DATA, farm.farmName, plots.size(), plots));
            }

            plotMultiPointOverlay.setItems(plotPointItems);
            plotMultiPointOverlay.setEnable(!isShowPlot);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doAfterSuccess(selectFarmDtos -> {
            //        selectFarmAdapter.refreshData(farms);
            plotExpandAdapter.refreshPlot(expandDataList);
        }).subscribe();


    }

    private View getPlotView(String plotName) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_plot_marker, null);
        view.setText(plotName);
        return view;
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
    protected void onStop() {
        super.onStop();
        isStop = true;
//        onMapClick(null);
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
        requestCount = 0;
        isRunning = false;
        disposable.dispose();

        machineData.clear();

        clearPlots();
        if (activeBroadcast != null) unregisterReceiver(activeBroadcast);
        if (clusterOverlay != null) clusterOverlay.onDestroy();
        if (locationMarker != null) locationMarker.destroy();
        if (locationBitmapDescriptor != null) locationBitmapDescriptor.recycle();
        if (plotMultiBitmapDescriptor != null) plotMultiBitmapDescriptor.recycle();
        if (plotMarkerIcon != null) plotMarkerIcon.recycle();
        if (descriptorOnLineSelect != null) descriptorOnLineSelect.recycle();
        if (descriptorOnLine != null) descriptorOnLine.recycle();
        if (descriptorOffLineSelect != null) descriptorOffLineSelect.recycle();
        if (descriptorOffLine != null) descriptorOffLine.recycle();
        if (mapView != null) mapView.onDestroy();
        if (binding != null) binding.unbind();
    }


    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected boolean isShowAlarming() {
        return true;
    }

    @Override
    protected void onAlarmListener() {
        startActivity(AlarmActivity.class);
    }

    //点击操作按钮进入操作界面
    public void operator(View view) {
        startActivity(OperatorActivity.class);
        //设置operator从底部进入
        overridePendingTransition(R.anim.act_slide_in_bottom, R.anim.act_slide_out_bottom);
    }

    //展示农机选择页面
    public void doubleLeft(View view) {
        binding.dlMonitor.openDrawer(binding.llMonitorSelectView.llMonitorSelectRoot);
    }

    //显示或关闭农机列表
    public void showOrCloseMachineList(View view) {
        boolean isSelected = !binding.llMonitorSelectView.tvShowOrCloseMachine.isSelected();
        binding.llMonitorSelectView.tvShowOrCloseMachine.setSelected(isSelected);
        viewModel.setShowOrHideMachine(isSelected);
    }

    //跳转实时监控界面
    public void monitoring(View view) {
        if (selectMachine == null || TextUtils.isEmpty(selectMachine.sn)
                || TextUtils.isEmpty(selectMachine.latitude) || TextUtils.isEmpty(selectMachine.longitude)) {
            Toast.makeText(this, "此农机无位置数据！", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(MonitoringActivity.MONITORING_SN, selectMachine.sn);
        bundle.putString(MonitoringActivity.MONITORING_NAME, selectMachine.name);
        bundle.putBoolean(MonitoringActivity.MONITORING_IS_ONLINE, selectMachine.status);
        bundle.putDouble(MonitoringActivity.MONITORING_LATITUDE, Double.valueOf(selectMachine.latitude));
        bundle.putDouble(MonitoringActivity.MONITORING_LONGITUDE, Double.valueOf(selectMachine.longitude));
        startActivity(MonitoringActivity.class, bundle);
    }

    //跳转至农机轨迹日期页面，选择日期后进入轨迹页面
    public void historyPath(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(PlayBackMonthDetailActivity.MACHINE_NAME, selectMachine.name);
        bundle.putString(PlayBackMonthDetailActivity.MACHINE_SN, selectMachine.sn);
        startActivity(PlayBackMonthDetailActivity.class, bundle);
    }

    //点击了定位按钮进行定位
    public void clickLocation(View view) {
        if (locationClient != null) {
            locationClient.startLocation();
        }
    }

    //放大地图
    public void onMapZoomIn(View view) {
        CameraUpdate update = CameraUpdateFactory.zoomIn();
        aMap.animateCamera(update);
    }

    //缩小地图
    public void onMapZoomOut(View view) {
        CameraUpdate update = CameraUpdateFactory.zoomOut();
        aMap.animateCamera(update);
    }


    @Override
    public void onUpOrDownScroll(int tag) {
        if (tag == 1 && !isShowBaseDataDetail) {
            animatorClBaseData.start();
            isShowBaseDataDetail = true;
        } else if (tag == 2 && isShowBaseDataDetail) {
            animatorClBaseData.reverse();
            isShowBaseDataDetail = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.dlMonitor.isDrawerOpen(GravityCompat.END)) {
            binding.dlMonitor.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.selectMachine = null;
        viewModel.setShowOrHideLocationView(true);
        updateMarkers(false, null);
        if (isShowBaseData) {
            viewModel.setShowOrHideBaseData(false);
            isShowBaseData = false;
            if (isShowBaseDataDetail) {
                animatorClBaseData.reverse();
                isShowBaseDataDetail = false;
            }
        }
        movePoints(deviceLatLngs);
    }

    @Override
    public void onMachineSelected(SelectMachineDto machineDto) {
        this.selectMachine = machineDto;
        //请求农机实时数据
        String today = TimeUtils.getTodayStr(new Date());
        viewModel.getDevicesPropers(MonitorActivity.this, machineDto.sn, today);
        //修改实时监控的按钮
//        viewModel.setMonitoringButton(machineDto.status);

        //显示农机信息
        Log.d(TAG, "onMachineSelected :" + machineDto.name);
        if (binding.dlMonitor.isDrawerOpen(GravityCompat.END)) {
            binding.dlMonitor.closeDrawers();
        }
        if (!isShowBaseData) {
            isShowBaseData = true;
            viewModel.setShowOrHideLocationView(false);
            viewModel.setShowOrHideBaseData(true);
        }

        //选中marker 并展示数据
        viewModel.setBaseData(machineDto);

        //显示选中的 marker
        updateMarkers(true, machineDto);
        //移动视角
        movePointCamera(true, machineDto.latLng);
    }


    //    @Override
    public void onPlotSelected(SelectFarmDto.SelectPlotDto plotDto) {
        if (plotDto == null) return;
        //显示地块及地块对应农机信息
        Log.d(TAG, "onPlotSelected : " + plotDto.fieldName);
        if (binding.dlMonitor.isDrawerOpen(GravityCompat.END)) {
            binding.dlMonitor.closeDrawers();
        }
        if (isShowBaseData) {
            isShowBaseData = false;
            viewModel.setShowOrHideLocationView(true);
            viewModel.setShowOrHideBaseData(false);
        }
        if (isShowBaseDataDetail) {
            animatorClBaseData.reverse();
            isShowBaseDataDetail = false;
        }
        updateMarkers(false, null);

        //绘制地块路径
        movePoints(plotDto.latLngs);
    }


    @Override
    public void onLocationChanged(AMapLocation l) {
        if (l.getErrorCode() == 0) {
            Log.d(TAG, "location success : " + l.getLatitude() + ", " + l.getLongitude());
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            //绘制marker
            if (locationMarker == null) {
                locationMarker = aMap.addMarker(locationMarkerOptions);
            }
            locationMarker.setPosition(latLng);

            //移动视角
            movePointCamera(false, latLng);
        } else {
            String errText = "定位失败," + l.getErrorCode() + ": " + l.getErrorInfo();
            Log.d(TAG, "location error : %s" + errText);
        }
    }

    //移动到聚合点
    private void movePoints(List<LatLng> latLngs) {
        if (latLngs == null || latLngs.size() == 0) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            boundsBuilder.include(latLng);
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), ANIMATE_BOUND));
    }

    /**
     * 移动视角到某个点
     *
     * @param zoom   是否放大
     * @param latLng latlng
     */
    private void movePointCamera(boolean zoom, LatLng latLng) {
        if (zoom) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ANIMATE_BOUND));
        } else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    }

    //清除设备所有marker点
//    private void clearMarkers(boolean isDestroy) {
//        if (!machineData.isEmpty()) {
//            for (SelectMachineDto machineDto : machineData) {
//                if (machineDto.marker != null) {
//                    if (!machineDto.marker.isRemoved()) machineDto.marker.remove();
//                    if (isDestroy) {
//                        if (machineDto.marker != null) machineDto.marker.destroy();
//                    }
//                }
//            }
//        }
//    }

    //清除地块
    private void clearPlots() {
        if (!plotDtoList.isEmpty()) {
            for (SelectFarmDto.SelectPlotDto plotDto : plotDtoList) {
                if (!plotDto.plotMarker.isRemoved()) plotDto.plotMarker.remove();
                if (plotDto.plotMarker != null) plotDto.plotMarker.destroy();
                plotDto.polygon.remove();
            }
            plotDtoList.clear();
        }
        if (plotMultiPointOverlay != null) plotMultiPointOverlay.remove();
        if (!plotPointItems.isEmpty()) plotPointItems.clear();
    }


    //添加marker点
    private int online = 0, offline = 0;
    private synchronized void addMarkersAndUpdateView() {
        //不在页面内不需要绘制
        if (isStop) return;

        deviceLatLngs.clear();
        clusterItems.clear();
        try {
            online = 0; offline = 0;
            Log.d(TAG, "addMarkersAndUpdateView: requestCount ==>" + requestCount);
            Single.just(1).doOnSubscribe(disposable -> {
               try {
                   for (SelectMachineDto dto : machineData) {
                       if (dto.status) {
                           online++;
                       } else {
                           offline++;
                       }
                       dto.brandAndModel = dto.productBrandMeaning + "-" + dto.productModel;
                       if (!TextUtils.isEmpty(dto.latitude) && !TextUtils.isEmpty(dto.longitude)) {
                           LatLng latLng = new LatLng(Double.valueOf(dto.latitude), Double.valueOf(dto.longitude));
                           deviceLatLngs.add(latLng);
                           dto.latLng = latLng;
                           String defaultAddress = dto.province + dto.city + dto.district;
                           dto.address = getAddress(Double.valueOf(dto.latitude), Double.valueOf(dto.longitude), defaultAddress);
                           if (dto.address != null && dto.address.length() > 19) {
                               dto.address = dto.address.substring(0, 19) + "\n" + dto.address.substring(19);
                           }

                           //封装聚合点
                           RegionItem regionItem = new RegionItem(latLng, dto.name, dto.province, dto.city, dto.district);
                           regionItem.id = dto.deviceId;
                           regionItem.isOnline = dto.status;

                           //设置是否选中
                           regionItem.isSelected = selectMachine != null && TextUtils.equals(selectMachine.sn, dto.sn);

                           clusterItems.add(regionItem);
                       }
                   }
               }catch (Exception e) {
                   e.printStackTrace();
               }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doAfterSuccess(integer -> {
                try {
                   if (!isStop) {
                       //聚合
                       clusterOverlay.updateMarkers(clusterItems);

                       selectMachineAdapter.refreshData(machineData);

                       viewModel.setDevicesNum(online, offline);
                       if (requestCount == 1) {
                           //移动视角
                           movePoints(deviceLatLngs);
                       }
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).subscribe();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private String getAddress(double latitude, double longitude, String defaultAddress) {
        try {
            MyCallable myCallable = new MyCallable(latitude, longitude, defaultAddress);
            FutureTask<String> task = new FutureTask<>(myCallable);
            executor.execute(task);
            return task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultAddress;
        }
    }

    //经纬度转为地址
    class MyCallable implements Callable<String> {

        private double lat;
        private double lng;
        private String defaultAddress;

        MyCallable(double lat, double lng, String defaultAddress) {
            this.lat = lat;
            this.lng = lng;
            this.defaultAddress = defaultAddress;
        }

        @Override
        public String call() {
            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                if (addressList != null && addressList.size() > 0 && addressList.get(0).getAddressLine(0) != null) {
                    return addressList.get(0).getAddressLine(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return defaultAddress;
        }
    }


    //更新maker选中
    private void updateMarkers(boolean notClickMap, SelectMachineDto dto) {
        for (SelectMachineDto machineDto : machineData) {
            if (notClickMap) {
                machineDto.selected = (dto.deviceId == machineDto.deviceId);
            } else {
                machineDto.selected = false;
            }
        }
        for (ClusterItem clusterItem : clusterItems) {
            if (notClickMap) {
                clusterItem.setSelected(dto.deviceId == clusterItem.getId());
            } else {
                clusterItem.setSelected(false);
            }
        }
        if (clusterOverlay != null) {
            clusterOverlay.updateMarkers(clusterItems);
        }
    }


    private BitmapDescriptor getDescriptor(boolean selected, boolean isOnline) {
        if (isOnline) {
            if (selected) return descriptorOnLineSelect;
            else return descriptorOnLine;
        } else {
            if (selected) return descriptorOffLineSelect;
            else return descriptorOffLine;
        }
    }

    private BitmapDescriptor getDescriptorView(boolean selected, boolean isOnline) {
        View view = LayoutInflater.from(this).inflate(R.layout.maker_view_image, null);
        int imageRes;
        if (isOnline) {
            if (selected) imageRes = R.drawable.icon_agricultural_machinery_online_select;
            else imageRes = R.drawable.icon_agricultural_machinery_online;
        } else {
            if (selected) imageRes = R.drawable.icon_agricultural_machinery_offline_select;
            else imageRes = R.drawable.icon_agricultural_machinery_offline;
        }
        ((ImageView) view.findViewById(R.id.iv_maker_image)).setImageResource(imageRes);
        return BitmapDescriptorFactory.fromView(view);
    }

    @Override
    public BitmapDescriptor getDrawAble(Cluster cluster) {
        return getDescriptor(cluster.isSelected, cluster.isOnline);
    }


    @Override
    public void onClusterClick(Marker marker, Cluster cluster) {
        if (cluster.clusterItems.size() == 1) {
            for (SelectMachineDto dto : machineData) {
                if (cluster.id == dto.deviceId) {
                    onMachineSelected(dto);
                    break;
                }
            }
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : cluster.clusterItems) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, ANIMATE_BOUND));
        }
    }


    @Override
    public void onCameraChangeFinish(float mapZoom) {
        Boolean isShowPlotCenter = mapZoom >= SHOW_PLOT_CENTER_ZOOM;
        if (isShowPlotCenter != isActualShowCenter) {
            isActualShowCenter = isShowPlotCenter;
            Log.d(TAG, "onCameraChangeFinish: 地块中心：" + mapZoom + " , " + isActualShowCenter);
            showPlotCenter(isActualShowCenter);
        }
    }


    private void showPlotCenter(boolean isShowCenter) {
        if (plotDtoList.size() > 0) {
            try {
                Single.just(isShowCenter).observeOn(Schedulers.io()).doOnSubscribe(aBoolean -> {
                    try {
                        for (SelectFarmDto.SelectPlotDto dto : plotDtoList) {
                            if (dto.plotMarker != null) dto.plotMarker.setVisible(isShowCenter);
                            if (dto.polygon != null) dto.polygon.setVisible(isShowCenter);
                        }
                        if (plotMultiPointOverlay != null) {
                            plotMultiPointOverlay.setEnable(!isShowCenter);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "showPlotCenter: " + e.getMessage());
                    }

                }).subscribe();
            } catch (Exception e) {
                Log.d(TAG, "showPlotCenter: " + e.getMessage());
            }
        }
    }

}
