package com.zhny.library.presenter.monitor.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityMonitoringBinding;
import com.zhny.library.presenter.monitor.helper.MonitorHelper;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SocketTrack;
import com.zhny.library.presenter.monitor.viewmodel.MonitoringViewModel;
import com.zhny.library.presenter.monitor.websocket.IReceiveMessage;
import com.zhny.library.presenter.monitor.websocket.WebSocketManager;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.work.dialog.ViewPictureDialog;
import com.zhny.library.presenter.work.dto.SinglePicture;
import com.zhny.library.presenter.work.listener.WorkTrackViewClickListener;
import com.zhny.library.utils.DataUtil;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;
import com.zhny.library.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class MonitoringActivity extends BaseActivity implements WorkTrackViewClickListener, AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, IReceiveMessage {
    private static final String TAG = "MonitoringActivity";

    public static final String MONITORING_NAME = "monitoring_monitoring_name";
    public static final String MONITORING_SN = "monitoring_monitoring_sn";
    public static final String MONITORING_IS_ONLINE = "monitoring_monitoring_is_online";
    public static final String MONITORING_LATITUDE = "monitoring_monitoring_latitude";
    public static final String MONITORING_LONGITUDE = "monitoring_monitoring_longitude";

    private String machineSn;
    private String machineName = "";
    private boolean isOnline;

    private ActivityMonitoringBinding binding;
    private MonitoringViewModel viewModel;
    private MapView mapView;
    private AMap aMap;
    private BitmapDescriptor bitmapDescriptor, picBitmapDescriptor, plotMarkerIcon;
    private PolygonOptions plotPolygonOptions;
    private MarkerOptions plotMarkerPolygonOptions;
    private PolylineOptions widthPolylineOptions, linePolylineOptions;
    private MovingPointOverlay smoothMarker;
    private HashMap<Polyline, Double> hashMap = new HashMap<>();
    private List<Polyline> widthPolylineList = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<SelectFarmDto.SelectPlotDto> plotDtoList = new ArrayList<>();
    private TileOverlay tileOverlay;
    private boolean openWidth, openPicture;

    private List<TrackInfo> trackInfoList = new ArrayList<>();
    private List<LatLng> latLngList = new ArrayList<>();
    private LatLng firstLatLng, lastLatLng;
    private static final int SINGLE_MOVE_SECONDS = 60;
    private int num = 0;

    private int offLineSecond; //快码值

    // addPolyline 绘制上限
    private static final int MAX_LINE_POINT_SIZE = 5000;


    private MarkerOptions picMarkerOptions;
    private List<String> imgUrls = new ArrayList<>();
    private List<Marker> imgMarkers = new ArrayList<>();
    private ViewPictureDialog viewPictureDialog;

    private Boolean isActualShowCenter;
    private static final int SHOW_PLOT_CENTER_ZOOM = 14;


    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            machineName = params.getString(MONITORING_NAME, "");
            machineSn = params.getString(MONITORING_SN, "");
            isOnline = params.getBoolean(MONITORING_IS_ONLINE, false);
            firstLatLng = getPoint(params.getDouble(MONITORING_LATITUDE), params.getDouble(MONITORING_LONGITUDE));
            Log.d(TAG, "initParams: " + firstLatLng.latitude + ", " + firstLatLng.longitude);
            params.clear();
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(MonitoringViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monitoring);

        mapView = binding.monitoringMapView;
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);

        viewPictureDialog = new ViewPictureDialog();

        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        binding.layoutWorkMonitorRightView.setViewModel(viewModel);
        binding.layoutWorkMonitorRightView.setClickListener(this);
        binding.setClickListener(this);

        setToolBarTitle(machineName);

        if (tileOverlay != null) {
            tileOverlay.remove();
        }

        tileOverlay = MapUtils.addRemoteOverlay(aMap);

        plotMarkerPolygonOptions = new MarkerOptions().anchor(0.5f, 0.5f);

        plotPolygonOptions = new PolygonOptions()
                .strokeWidth(DisplayUtils.dp2px(1.0f))
                .strokeColor(Color.parseColor("#FFB100"))
                .fillColor(Color.parseColor("#00000000")).zIndex(990);

        widthPolylineOptions = new PolylineOptions()
                .width(DisplayUtils.dp2px(5.2f))
                .color(Color.parseColor("#52999999"));

        linePolylineOptions = new PolylineOptions().color(Color.parseColor("#FF0000"))
                .width(DisplayUtils.dp2px(1.0f));

        View picView = LayoutInflater.from(this).inflate(R.layout.maker_pic_image, null);
        ((ImageView) picView.findViewById(R.id.iv_pic_image)).setImageResource(R.drawable.icon_img);
        picBitmapDescriptor = BitmapDescriptorFactory.fromView(picView);
        picMarkerOptions = new MarkerOptions().icon(picBitmapDescriptor).anchor(0.5f, 0.5f).zIndex(991);


        View view = LayoutInflater.from(this).inflate(R.layout.maker_view_image, null);
        int imageRes = isOnline ? R.drawable.icon_play_back_marker_online : R.drawable.icon_play_back_marker;
        ((ImageView) view.findViewById(R.id.iv_maker_image)).setImageResource(imageRes);
        bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmapDescriptor).anchor(0.5f, 0.5f);
        Marker moveMarker = aMap.addMarker(markerOptions);
        moveMarker.setZIndex(999);
        smoothMarker = new MovingPointOverlay(aMap, moveMarker);
    }

    @Override
    protected void onStart() {
        super.onStart();

        offLineSecond = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.FINALVALUE.OFF_LINE_SECOND_CODE, Constant.FINALVALUE.OFF_LINE_SECOND);
        Log.d(TAG, "onStart: 获取快码值 OFF_LINE_SECOND_CODE ==> " + offLineSecond);

        openPicture = SPUtils.getInstance(Constant.SP.SP_NAME).getBoolean(Constant.SP.WORK_TRACK_OPEN_PICTURE, Constant.SP.WORK_TRACK_OPEN_PICTURE_DEFAULT);
        viewModel.showPicture.setValue(openPicture);
        openWidth = SPUtils.getInstance(Constant.SP.SP_NAME).getBoolean(Constant.SP.WORK_TRACK_OPEN_WIDTH, Constant.SP.WORK_TRACK_OPEN_WIDTH_DEFAULT);
        viewModel.showWidth.setValue(openWidth);

        if (trackInfoList.size() == 0) {
            getTrackInfo();
        }
        if (plotDtoList.size() == 0) {
            getPlotData();
        }
        if (imgUrls.size() == 0) {
            getPictures();
        }

        // 实时监测数据变化
        startMonitorData();
    }

    //轨迹 -> 请求轨迹数据
    private void getTrackInfo() {
        showLoading();
        String startTime = TimeUtils.getTodayZero();
        String endTime = TimeUtils.getCurTime();
        viewModel.getTrackInfo(loadingDialog, machineSn, startTime, endTime).observe(this, listBaseDto -> {
                    dismissLoading();
                    try {
                        if (listBaseDto.getContent() != null) {
                            if (listBaseDto.getContent().size() != 0) {
                                trackInfoList.clear();
                                trackInfoList.addAll(listBaseDto.getContent());
                                //数据排序 防止数据量大的时候发生异常
                                Collections.sort(trackInfoList, (o1, o2) -> o1.trackDate.compareTo(o2.trackDate));
                            }
                        }

                        //设置路线及进度
                        renderPolyline();
                        movePoint();
//                        startTest();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    //绘制原色线
    private void renderPolyline() {
        if (trackInfoList.size() == 0) return;
        latLngList.clear();
        hashMap.clear();
        widthPolylineList.clear();

        String startTime = "";
        //记录相同状态的点
        List<LatLng> tmpSamePoints = new ArrayList<>();
        for (int i = 0; i < trackInfoList.size(); i++) {
            //获取当前和下一次的值
            LatLng curPoint = getPoint(trackInfoList.get(i).latitude, trackInfoList.get(i).longitude);
            String curWorkingState = trackInfoList.get(i).workingState;
            long curPosition = TimeUtils.convertSecond(trackInfoList.get(i).trackDate);
            float width = trackInfoList.get(i).width;
            LatLng nextPoint = curPoint;
            String nextWorkingState = curWorkingState;
            long nextPosition = curPosition;
            if (i == trackInfoList.size() - 1) {
                firstLatLng = curPoint;
            }
            if (i < trackInfoList.size() - 1) {
                nextPoint = getPoint(trackInfoList.get(i + 1).latitude, trackInfoList.get(i + 1).longitude);
                nextWorkingState = trackInfoList.get(i + 1).workingState;
                nextPosition = TimeUtils.convertSecond(trackInfoList.get(i + 1).trackDate);
            }
            latLngList.add(curPoint);
            if (i < trackInfoList.size() - 1) {
                //超过3分钟添加离线图标
                if (nextPosition - curPosition > offLineSecond) {
                    //离线标志
                    addOfflineMarker(curPoint);
                } else {
                    //未超60画轨迹 判断是否是相同状态
                    if (TextUtils.equals(curWorkingState, nextWorkingState)) {
                        if ("".equals(startTime)) {
                            startTime = trackInfoList.get(i).trackDate;
                        }
                        tmpSamePoints.add(curPoint);
                    } else {
                        //画颜色线
                        addColorLine(tmpSamePoints, curWorkingState, width);
                        tmpSamePoints.add(curPoint);
                        tmpSamePoints.add(nextPoint);
                        startTime = "";
                        tmpSamePoints.clear();
                    }
                }
            } else {
                //最后一个是终点 添加离线图标
                addOfflineMarker(curPoint);
                //最后一个和前面对比
                long prePosition = TimeUtils.convertSecond(trackInfoList.get(i - 1).trackDate);
                if (curPosition - prePosition <= offLineSecond) {
                    tmpSamePoints.add(curPoint);
                    //画线
                    addColorLine(tmpSamePoints, curWorkingState, width);
                }
            }
        }
        movePoint();
    }

    private void movePoint() {
        //初始添加点
        Log.d(TAG, "renderPolyline: " + firstLatLng.latitude + ", " + firstLatLng.longitude);
        smoothMarker.setPosition(firstLatLng);
        if (latLngList.size() == 0) {
            movePoints(Collections.singletonList(firstLatLng));
        } else {
            movePoints(latLngList);
        }
    }


    //请求照片数据
    private void getPictures() {
        String today = TimeUtils.getTodayStr(new Date());
        String startTime = today.concat(" ").concat("00:00:00");
        String endTime = today.concat(" ").concat("23:59:59");
        viewModel.getPictureData(machineSn, startTime, endTime).observe(this, baseDto -> {
            List<SinglePicture> pictureDtos = baseDto.getContent();
            if (pictureDtos != null && pictureDtos.size() > 0) {
                showPicture(pictureDtos);
            }
        });
    }

    //添加图片的marker点
    private void showPicture(List<SinglePicture> pictureDtos) {
        imgUrls.clear();
        for (Marker imgMarker : imgMarkers) {
            imgMarker.remove();
        }
        imgMarkers.clear();
        for (SinglePicture pictureDto : pictureDtos) {
            imgUrls.add(pictureDto.url);
            LatLng latLng = new LatLng(pictureDto.latitude, pictureDto.longitude);
            Marker marker = aMap.addMarker(picMarkerOptions);
            marker.setPosition(latLng);
            marker.setVisible(openPicture);
            imgMarkers.add(marker);
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

    //绘制所有地块并移动至中心
    private void drawPlots(List<SelectFarmDto> farms) {
        clearPlots(false);
        float zoom = aMap.getCameraPosition().zoom;
        for (SelectFarmDto farm : farms) {
            for (SelectFarmDto.SelectPlotDto plotDto : farm.fieldList) {
                if (!TextUtils.isEmpty(plotDto.center)) {
                    String[] str = plotDto.center.split(",");
                    LatLng centerLatLng = new LatLng(Double.valueOf(str[1]), Double.valueOf(str[0]));

                    //数据过滤
                    float distance = AMapUtils.calculateLineDistance(firstLatLng, centerLatLng);
                    if (distance > Constant.FINALVALUE.DEVICE_DISTANCE) continue;

                    //① 地块绘制
                    plotDto.latLngs = MonitorHelper.getLatLngs(plotDto.coordinates);
                    Polygon polygon = aMap.addPolygon(plotPolygonOptions);
                    polygon.setPoints(plotDto.latLngs);
                    plotDto.polygon = polygon;

                    //② 中心点绘制
                    plotMarkerIcon = BitmapDescriptorFactory.fromView(getPlotView(plotDto.fieldName));
                    Marker plotCenterMarker = aMap.addMarker(plotMarkerPolygonOptions);
                    plotCenterMarker.setPosition(centerLatLng);
                    plotCenterMarker.setIcon(plotMarkerIcon);
                    plotCenterMarker.setVisible(zoom >= SHOW_PLOT_CENTER_ZOOM);
                    plotDto.plotMarker = plotCenterMarker;

                    plotDtoList.add(plotDto);
                }
            }
        }
    }

    private View getPlotView(String plotName) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_plot_marker, null);
        view.setText(plotName);
        return view;
    }

    //实时监测数据变化
    private void startMonitorData() {
        if (TextUtils.isEmpty(machineSn)) return;
        String url = Constant.Server.WEB_SOCKET_URL + machineSn;
        executorService.execute(() -> WebSocketManager.getInstance().initConnect(url, this));
    }


//    test method
//    @SuppressLint("CheckResult")
//    private void startTest() {
//        ObservableRange.interval(5, SINGLE_MOVE_SECONDS, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(aLong -> {
//                    SocketTrack socketTrack = TestData.getSocketTrackData(30, null);
//                    Log.d(TAG, "interval :" + aLong + " ==> 请求数据:" + socketTrack.toString());
//                    refreshView(socketTrack);
//                });
//    }

    private void refreshView(SocketTrack track) {
        num++;
        if (track != null && TextUtils.equals(track.ifaceId, "1003")) {
            runOnUiThread(() -> {
                SocketTrack.ReportedBean reported = track.reported;
                if (reported != null) {
                    float width = TextUtils.isEmpty(reported.width) ? 0f : Float.valueOf(reported.width);
                    List<LatLng> listPoints = getListPoints(reported.posList);
                    //绘制颜色线
                    if (num == 1) {
                        //添加连线数据
                        listPoints.add(0, firstLatLng);
                        addColorLine(listPoints, "running", width);
                        smoothMarker.setPoints(listPoints);
                    } else {
                        listPoints.add(0, lastLatLng);
                        addColorLine(listPoints, "running", width);
                        smoothMarker.setPoints(listPoints);
                    }
                    if (listPoints.size() > 1) {
                        lastLatLng = listPoints.get(listPoints.size() - 1);
                    }
                    smoothMarker.setTotalDuration(SINGLE_MOVE_SECONDS);
                    smoothMarker.startSmoothMove();
                }
            });
        }
    }

    private List<LatLng> getListPoints(List<SocketTrack.ReportedBean.PosListBean> posList) {
        List<LatLng> points = new ArrayList<>();
        double speed = 0;
        if (posList != null) {
            for (SocketTrack.ReportedBean.PosListBean pos : posList) {
                if (TextUtils.isEmpty(pos.latitude) || TextUtils.isEmpty(pos.longitude)) continue;
                points.add(new LatLng(Double.valueOf(pos.latitude), Double.valueOf(pos.longitude)));
                speed += TextUtils.isEmpty(pos.speed) ? 0d : Double.valueOf(pos.speed);
            }
        }
        String avgSpeedStr = DataUtil.get2Point(points.size() > 0 ? speed / points.size() : 0d);
        binding.tvMonitoringSpeed.setText(avgSpeedStr);
        return points;
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
    protected void onStop() {
        super.onStop();
        WebSocketManager.getInstance().close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearPlots(true);
        mapView.onDestroy();
        if (plotMarkerIcon != null) plotMarkerIcon.recycle();
        if (bitmapDescriptor != null) bitmapDescriptor.recycle();
        if (picBitmapDescriptor != null) picBitmapDescriptor.recycle();
        if (binding != null) binding.unbind();
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    //离线图标
    private void addOfflineMarker(LatLng latLng) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_img_marker, null);
        MarkerOptions options = new MarkerOptions().position(latLng).anchor(0f, 0.5f).icon(BitmapDescriptorFactory.fromView(view));
        aMap.addMarker(options).setZIndex(995);
    }

    //获取坐标LatLng
    private LatLng getPoint(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    //移动到聚合点
    private void movePoints(List<LatLng> latLngList) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList) {
            boundsBuilder.include(latLng);
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), DisplayUtils.dp2px(80)));
    }


    //轨迹 -> map -> 颜色线 working or running
    private void addColorLine(List<LatLng> points, String workType, float width) {
        if (points != null && points.size() > MAX_LINE_POINT_SIZE) {
            List<List<LatLng>> groupList = MapUtils.groupList(points, MAX_LINE_POINT_SIZE);
            for (List<LatLng> latLngs : groupList) {
                drawLine(latLngs, width);
            }
        } else {
            drawLine(points, width);
        }
    }

    private void drawLine(List<LatLng> points, double width) {
        Polyline widthPolyline = aMap.addPolyline(widthPolylineOptions);
        widthPolyline.setPoints(points);
        widthPolyline.setZIndex(996);
        widthPolylineList.add(widthPolyline);
        hashMap.put(widthPolyline, width);
        widthPolyline.setVisible(openWidth);

        //绘制轨迹
        Polyline polyline = aMap.addPolyline(linePolylineOptions);
        polyline.setPoints(points);
        polyline.setZIndex(996);
    }


    //清除地块
    private void clearPlots(boolean isDestroy) {
        if (!plotDtoList.isEmpty()) {
            try {
                for (SelectFarmDto.SelectPlotDto plotDto : plotDtoList) {
                    if (plotDto.plotMarker != null && !plotDto.plotMarker.isRemoved())
                        plotDto.plotMarker.remove();
                    if (plotDto.plotMarker != null && isDestroy) plotDto.plotMarker.destroy();
                    plotDto.polygon.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            plotDtoList.clear();
        }
    }

    @Override
    public void onSwitchPicture() {
        openPicture = !openPicture;
        viewModel.showPicture.setValue(openPicture);
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_OPEN_PICTURE, openPicture);
        if (imgMarkers.size() > 0) {
            List<LatLng> latLngs = new ArrayList<>(imgMarkers.size());
            for (Marker imgMarker : imgMarkers) {
                latLngs.add(imgMarker.getPosition());
                imgMarker.setVisible(openPicture);
            }
            if (openPicture) {
                movePoints(latLngs);
            }
        }
    }

    @Override
    public void onSwitchWidth() {
        openWidth = !openWidth;
        viewModel.showWidth.setValue(openWidth);
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_OPEN_WIDTH, openWidth);
        if (widthPolylineList.size() > 0) {
            for (Polyline p : widthPolylineList) {
                p.setVisible(openWidth);
            }
        }
    }

    @Override
    public void onZoom(boolean isZoomIn) {
        if (isZoomIn) {
            CameraUpdate update = CameraUpdateFactory.zoomIn();
            aMap.animateCamera(update);
        } else {
            CameraUpdate update = CameraUpdateFactory.zoomOut();
            aMap.animateCamera(update);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    /**
     * @param cameraPosition 地图比例尺变化完成监听
     *                       设置幅宽随比例尺变化而变化
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //幅宽改变
        if (widthPolylineList != null && widthPolylineList.size() > 0) {
            float scalePerPixel = aMap.getScalePerPixel();
            float d = 1 / scalePerPixel;
            for (Polyline p : widthPolylineList) {
                Double width = hashMap.get(p);
                if (width != null) {
                    float result = (float) (d * width / 100);
                    //result为幅宽实际在地图中的宽度,DisplayUtils.dp2px(1.0f)为轨迹宽度
                    p.setWidth(result + DisplayUtils.dp2px(1.0f) * 1.2f);
                }
            }
        }

        //地块中心marker显示
        Boolean isShowPlotCenter = cameraPosition.zoom >= SHOW_PLOT_CENTER_ZOOM;
        if (isShowPlotCenter != isActualShowCenter) {
            isActualShowCenter = isShowPlotCenter;
            Log.d(TAG, "onCameraChangeFinish: 地块中心：" + cameraPosition.zoom + " , " + isActualShowCenter);
            showPlotCenter(isActualShowCenter);
        }
    }

    private void showPlotCenter(boolean isShowCenter) {
        if (plotDtoList.size() > 0) {
            for (SelectFarmDto.SelectPlotDto dto : plotDtoList) {
                if (dto.plotMarker != null) dto.plotMarker.setVisible(isShowCenter);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //点击查看照片
        if (openPicture && !imgMarkers.isEmpty() && !imgUrls.isEmpty()) {
            LatLng position = marker.getPosition();
            for (int i = 0; i < imgMarkers.size(); i++) {
                LatLng markerPosition = imgMarkers.get(i).getPosition();
                if (position.longitude == markerPosition.longitude && position.latitude == markerPosition.latitude) {
                    viewPictureDialog.setParams(i, imgUrls);
                    viewPictureDialog.show(getSupportFragmentManager(), null);
                    break;
                }
            }
        }
        return false;
    }


    //======================== webSocket =================================
    @Override
    public void onConnectSuccess() {
        Log.d(TAG, "==> onConnectSuccess");
    }

    @Override
    public void onConnectFailed() {
        Log.d(TAG, "==> onConnectFailed");
    }

    @Override
    public void onConnectFailedOver() {
        runOnUiThread(() -> Toast.makeText(MonitoringActivity.this, getString(R.string.get_web_socket_msg_err), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClose() {
        Log.d(TAG, "==> onClose");
    }

    @Override
    public void onMessage(String msg) {
        Log.d(TAG, "==> onMessage :" + msg + ", num:" + num);
        try {
            SocketTrack socketTrack = JSON.parseObject(msg, SocketTrack.class);
            refreshView(socketTrack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
