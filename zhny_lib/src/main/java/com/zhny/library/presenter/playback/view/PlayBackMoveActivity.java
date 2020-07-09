package com.zhny.library.presenter.playback.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityPlaybackMoveBinding;
import com.zhny.library.presenter.monitor.helper.MonitorHelper;
import com.zhny.library.presenter.playback.PlayBackConstants;
import com.zhny.library.presenter.playback.customer.RangeSeekBar;
import com.zhny.library.presenter.playback.customer.SingleBottomPopWin;
import com.zhny.library.presenter.playback.listener.OnClickListener;
import com.zhny.library.presenter.playback.model.ColorInfo;
import com.zhny.library.presenter.playback.model.LandInfo;
import com.zhny.library.presenter.playback.model.MyPlay;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.viewmodel.PlayBackMoveViewModel;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;
import com.zhny.library.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class PlayBackMoveActivity extends BaseActivity implements OnClickListener, SingleBottomPopWin.OnSingleBottomPopWinListener,
        AMap.OnCameraChangeListener, RangeSeekBar.SelectScaleListener {

    private static final String TAG = "PlaybackMoveActivity";

    private PlayBackMoveViewModel viewModel;
    private ActivityPlaybackMoveBinding binding;
    private AMap aMap;
    private TileOverlay tileOverlay;
    private MovingPointOverlay smoothMarker;
    private BitmapDescriptor bitmapDescriptor;
    private PolylineOptions polylineOptions;

    private List<TrackInfo> trackInfoList = new ArrayList<>();
    private List<ColorInfo> colorInfoList = new ArrayList<>();

    private int speedDefaultIndex = 1;

    //播放状态，时间
    private int mPlayState = 0; //0:暂停 1:播放中
    private float SPEED_TYPE = Constant.FINALVALUE.SPEED_TIME;
    private SingleBottomPopWin singleBottomPopWin;
    private List<String> speedList;

    private String name, sn, startDate, endDate;
    private List<LatLng> latLngList = new ArrayList<>();

    private float showLeftPosition = 0f;
    //    private int currentIndex = 0;
    private boolean isDrag = false;
    private boolean isRangeLoading = false;
    private String rangeLoadingTime = "";

    private int offLineSecond; //快码值

    // addPolyline 绘制上限
    private static final int MAX_LINE_POINT_SIZE = 5000;

    private List<Marker> plotMarker = new ArrayList<>();
    private Boolean isActualShowCenter;
    private static final int SHOW_PLOT_CENTER_ZOOM = 14;
    private LatLng pathCenter;

    private PolygonOptions polygonOptions;
    private MarkerOptions plotCenterOptions;


    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            name = params.getString(PlayBackConstants.PLAY_BACK_MOVE_AN);
            sn = params.getString(PlayBackConstants.PLAY_BACK_MOVE_NAME);
            startDate = params.getString(PlayBackConstants.PLAY_BACK_MOVE_START_DATE);
            endDate = params.getString(PlayBackConstants.PLAY_BACK_MOVE_END_DATE);
            params.clear();
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(PlayBackMoveViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playback_move);

        binding.map.onCreate(savedInstanceState);
        aMap = binding.map.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);

        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        setToolBarTitle(name);
        binding.setClickListener(this);

        if (tileOverlay != null) tileOverlay.remove();
        tileOverlay = MapUtils.addRemoteOverlay(aMap);

        speedList = Arrays.asList(PlayBackConstants.SPEED_LEVEL_ONE, PlayBackConstants.SPEED_LEVEL_TWO, PlayBackConstants.SPEED_LEVEL_THREE, PlayBackConstants.SPEED_LEVEL_FOUR);
        singleBottomPopWin = new SingleBottomPopWin(this, speedList, this);

        View view = LayoutInflater.from(this).inflate(R.layout.maker_view_image, null);
        int imageRes = R.drawable.icon_play_back_marker;
        ((ImageView) view.findViewById(R.id.iv_maker_image)).setImageResource(imageRes);
        bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmapDescriptor).anchor(0.5f, 0.5f);
        Marker moveMarker = aMap.addMarker(markerOptions);
        moveMarker.setZIndex(999);
        smoothMarker = new MovingPointOverlay(aMap, moveMarker);

        polygonOptions = new PolygonOptions().strokeWidth(DisplayUtils.dp2px(1f))
                .strokeColor(getResources().getColor(R.color.color_FFB100))
                .fillColor(Color.parseColor("#00000000"))
                .zIndex(995);

        plotCenterOptions = new MarkerOptions().zIndex(996);

        polylineOptions = new PolylineOptions().width(DisplayUtils.dp2px(1.0f)).zIndex(997);

        binding.rangeSeekBar.setSelectScaleListener(this);
    }

    @Override
    public void onScaleChange(long progress) {
        String curTime = TimeUtils.getTimeMs(progress);
        binding.tvCurTime.setText(curTime);

        if (progress == RangeSeekBar.SCALE_MAX) {
            closeRangeLoading();
        } else if (progress < RangeSeekBar.SCALE_MAX) {
            if (TextUtils.equals(rangeLoadingTime, curTime)) {
                rangeLoadingTime = "";
                closeRangeLoading();
            } else {
                rangeLoadingTime = TimeUtils.getTimeMs(progress + 60);
            }
        }
        if (trackInfoList == null || trackInfoList.size() == 0) {
            return;
        }
        if (progress == RangeSeekBar.SCALE_MAX) {
            //设置按钮 暂停
            binding.playBtn.setBackgroundResource(R.drawable.icon_circleplay);
            mPlayState = 0;
        }
        moveToPosition(progress);
    }

    @Override
    public void onRangePlay() {
        isDrag = true;
        if (mPlayState == 0) {
            startOrStopMenu(binding.playBtn);
        }
        if (mPlayState == 1) {
            isRangeLoading = true;
            if (smoothMarker != null) {
                smoothMarker.stopMove();
            }
            //开启缓冲
            showLoading();
        }
    }

    //取消缓冲
    private void closeRangeLoading() {
        if (isRangeLoading && loadingDialog != null) {
            dismissLoading();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        offLineSecond = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.FINALVALUE.OFF_LINE_SECOND_CODE, Constant.FINALVALUE.OFF_LINE_SECOND);
        Log.d(TAG, "onStart: 获取快码值 OFF_LINE_SECOND_CODE ==> " + offLineSecond);

        if (trackInfoList.size() == 0) {
            requestTrackInfo();
        }
    }

    //轨迹 -> 请求轨迹数据
    private void requestTrackInfo() {
        showLoading();
//        Log.d(TAG, "【获取轨迹数据】" + "【" + sn + "】" + "【" + startDate + " - " + endDate + "】");
        viewModel.getTrackInfo(loadingDialog, sn, startDate, endDate).observe(this, listBaseDto -> {
                    dismissLoading();
                    try {
                        if (listBaseDto.getContent() != null) {
                            if (listBaseDto.getContent().size() != 0) {
                                trackInfoList.clear();
                                trackInfoList.addAll(listBaseDto.getContent());
                                //数据排序 防止数据量大的时候发生异常
                                Collections.sort(trackInfoList, (o1, o2) -> o1.trackDate.compareTo(o2.trackDate));
                                //设置路线及进度
                                renderPolyline();
                            }
                        }
                    } catch (Exception e) {
                        dismissLoading();
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    //轨迹 -> 设置路线及进度
    private ArrayList<MyPlay> myPlayList = new ArrayList<>();

    private void renderPolyline() {
        myPlayList.clear();
        if (trackInfoList.size() == 0) return;
        long startPosition = 0, endPosition;
        String startTime = "";

        //记录相同状态的点
        List<LatLng> tmpSamePoints = new ArrayList<>();
        //离线进度条3分钟内 上一次状态延续
        List<LatLng> stateContinuePoints = new ArrayList<>();

        long firstPosition = 0;

        colorInfoList.clear();
        latLngList.clear();

        for (int i = 0; i < trackInfoList.size(); i++) {
            //获取当前和下一次的值
            LatLng curPoint = getPoint(trackInfoList.get(i).latitude, trackInfoList.get(i).longitude);
            String curWorkingState = trackInfoList.get(i).workingState;
            long curPosition = TimeUtils.convertSecond(trackInfoList.get(i).trackDate);
            trackInfoList.get(i).position = curPosition;

            LatLng nextPoint = curPoint;
            String nextWorkingState = curWorkingState;
            long nextPosition = curPosition;
            if (i < trackInfoList.size() - 1) {
                nextPoint = getPoint(trackInfoList.get(i + 1).latitude, trackInfoList.get(i + 1).longitude);
                nextWorkingState = trackInfoList.get(i + 1).workingState;
                nextPosition = TimeUtils.convertSecond(trackInfoList.get(i + 1).trackDate);
            }
            latLngList.add(curPoint);


            if (i == 0) {
                //第一个点之前都是离线 进度条离线灰色
                endPosition = curPosition;
                addSeekBarColor(Constant.FINALVALUE.OFF_LINE, startPosition, endPosition);
                startPosition = endPosition;

                firstPosition = curPosition;

                //初始添加点
                smoothMarker.setPosition(curPoint);
            } else if (i < trackInfoList.size() - 1) {
                //超过3分钟添加离线图标
                if (nextPosition - curPosition > offLineSecond) {
                    //离线标志
                    addOfflineMarker(curPoint);

                    //离线进度条3分钟内 上一次状态延续
                    endPosition = (curPosition + offLineSecond);
                    //当前位置延续3分钟画对应的色
                    addSeekBarColor(curWorkingState, startPosition, endPosition);

                    stateContinuePoints.add(curPoint);
                    stateContinuePoints.add(nextPoint);
                    MyPlay myPlay = new MyPlay(curWorkingState, startPosition, endPosition, new ArrayList<>(stateContinuePoints));
                    myPlayList.add(myPlay);
                    stateContinuePoints.clear();

                    //3分钟之外 画离线
                    startPosition = endPosition;
                    endPosition = nextPosition;
                    addSeekBarColor(Constant.FINALVALUE.OFF_LINE, startPosition, endPosition);

                    startPosition = endPosition;
                } else {
                    //未超60画轨迹 判断是否是相同状态
                    if (TextUtils.equals(curWorkingState, nextWorkingState)) {
                        stateContinuePoints.add(curPoint);
                        if ("".equals(startTime)) {
                            startTime = trackInfoList.get(i).trackDate;
                        }
                        tmpSamePoints.add(curPoint);
                    } else {
                        stateContinuePoints.clear();
                        //设置终点
                        endPosition = curPosition;
                        //画颜色线
                        addColorLine(tmpSamePoints, curWorkingState);

                        tmpSamePoints.add(curPoint);
                        tmpSamePoints.add(nextPoint);

                        MyPlay myPlay = new MyPlay(curWorkingState, startPosition, endPosition, new ArrayList<>(tmpSamePoints));
                        myPlayList.add(myPlay);

                        startTime = "";
                        tmpSamePoints.clear();

                        //绘制进度条
                        addSeekBarColor(curWorkingState, startPosition, endPosition);
                        //终点变为下一段的起点
                        startPosition = endPosition;
                    }
                }
            } else {
                //最后一个是终点 添加离线图标
                addOfflineMarker(curPoint);
                //设置终点
                endPosition = curPosition;
                //最后一个和前面对比
                long prePosition = TimeUtils.convertSecond(trackInfoList.get(i - 1).trackDate);
                if (curPosition - prePosition <= offLineSecond) {
                    tmpSamePoints.add(curPoint);
                    tmpSamePoints.add(nextPoint);
                    //画线
                    addColorLine(tmpSamePoints, curWorkingState);
                    //绘制进度条
                    addSeekBarColor(curWorkingState, startPosition, endPosition);

                    MyPlay myPlay = new MyPlay(curWorkingState, startPosition, endPosition, new ArrayList<>(tmpSamePoints));
                    myPlayList.add(myPlay);

                } else {
                    tmpSamePoints.add(curPoint);
                    tmpSamePoints.add(nextPoint);

                    startPosition = curPosition;
                    endPosition = (curPosition + offLineSecond);
                    //当前位置画对应的色
                    addSeekBarColor(curWorkingState, startPosition, endPosition);
                }
                //最后一个和终点 离线 绘制进度条
                addSeekBarColor(Constant.FINALVALUE.OFF_LINE, endPosition, Constant.FINALVALUE.SECOND_SINGLE_DAY);
            }
        }

        movePoints(latLngList);
//        Log.d(TAG, "trackInfoList size: " + trackInfoList.size() + " , trackInfoList 0 >> " + trackInfoList.get(0));
//        Log.d(TAG, "playInfoList size: " + myPlayList.size() + " , myPlayList >> " + myPlayList);
//        Log.d(TAG, "colorInfoList size: " + colorInfoList.size() + " , colorInfoList >> " + colorInfoList);
        //画进度条
        binding.rangeSeekBar.setColorData(colorInfoList);
//        binding.rangeSeekBar.scrollerToProgress(0);
        if (firstPosition > 5) {
            firstPosition -= 5;
        }
        binding.rangeSeekBar.firstMoveTo(firstPosition);


        //请求地块数据
        requestLandInfo();
    }


    //获取地块
    private void requestLandInfo() {
        viewModel.getLandInfo().observe(this, listBaseDto -> {
            if (listBaseDto.getContent() != null) {
                List<LandInfo> landList = listBaseDto.getContent().content;
                if (landList != null && landList.size() > 0) {
                    float zoom = aMap.getCameraPosition().zoom;
                    Single.just(landList).observeOn(Schedulers.io()).doOnSubscribe(disposable -> {

                        clearPlotMarkers(false);
                        //获取轨迹中心点
                        pathCenter = MapUtils.getTheAreaCenter(latLngList);
                        Log.d(TAG, "renderPolyline: 轨迹中心点：" + pathCenter);

                        for (LandInfo landInfo : landList) {
                            if (!TextUtils.isEmpty(landInfo.center)) {
                                renderPolygon(landInfo, zoom);
                            }
                        }
                    }).subscribe();
                }
            }
        });
    }

    // 地块 -> 多边形
    private void renderPolygon(LandInfo landInfo, float zoom) {
        double latitude = (new BigDecimal(landInfo.center.split(",")[1])).doubleValue();
        double longitude = (new BigDecimal(landInfo.center.split(",")[0])).doubleValue();
        LatLng centerPlot = getPoint(latitude, longitude);
        float distance = AMapUtils.calculateLineDistance(pathCenter, centerPlot);
        if (distance <= Constant.FINALVALUE.DEVICE_DISTANCE) {
            //绘制多边形
            Polygon polygon = aMap.addPolygon(polygonOptions);
            polygon.setPoints(MonitorHelper.getLatLngs(landInfo.coordinates));

            //地块名称的marker
            View view = LayoutInflater.from(this).inflate(R.layout.layout_text_marker, null);
            ((TextView) view.findViewById(R.id.text)).setText(landInfo.fieldName);
            //将数据添加到地图上
            Marker marker = aMap.addMarker(plotCenterOptions);
            marker.setPosition(centerPlot);
            marker.setIcon(BitmapDescriptorFactory.fromView(view));
            marker.setVisible(zoom >= SHOW_PLOT_CENTER_ZOOM);
            plotMarker.add(marker);
        }
    }

    private void clearPlotMarkers(boolean isDestroy) {
        for (Marker marker : plotMarker) {
            if (marker != null && !marker.isRemoved()) marker.remove();
            if (isDestroy && marker != null) marker.destroy();
        }
    }


    //移动到当前位置
    private void moveToPosition(long progress) {
        if (trackInfoList != null && trackInfoList.size() > 0) {
            if (progress - trackInfoList.get(trackInfoList.size() - 1).position > 0) {
//                binding.layoutPlayBackLeftView.leftLayout.setVisibility(View.GONE);
                viewModel.showLeftLayout(false);
            } else if ((progress - trackInfoList.get(0).position < 0)) {
//                binding.layoutPlayBackLeftView.leftLayout.setVisibility(View.GONE);
                viewModel.showLeftLayout(false);
            } else {
                //进度等于当前
                for (int i = 0; i < trackInfoList.size(); i++) {
                    //计算有状态的时间点的刻度
                    long time = trackInfoList.get(i).position;
                    if (progress - time < 0) {
//                        Log.d(TAG, "moveToPosition: " + progress + ", time: " + time);
                        break;
                    } else if (progress - time == 0) {
//                        currentIndex = i;
                        calculateToMove((int) progress);
//                        binding.layoutPlayBackLeftView.leftLayout.setVisibility(View.VISIBLE);
                        viewModel.showLeftLayout(false);
                        boolean b = !trackInfoList.get(i).workingState.equals(Constant.FINALVALUE.OFF_LINE);
                        viewModel.showLeftLayout(b);

                        showLeftPosition = trackInfoList.get(i).position;
                        if (trackInfoList.get(i).jobTypeDetail != null) {
//                            binding.layoutPlayBackLeftView.name.setVisibility(View.VISIBLE);
                            viewModel.showLeftName(true);
                            viewModel.setWorkName(trackInfoList.get(i).jobTypeDetail);
                        } else {
                            viewModel.showLeftName(false);
//                            binding.layoutPlayBackLeftView.name.setVisibility(View.GONE);
//                            binding.layoutPlayBackLeftView.name.setText("");
                        }
                        String speedStr = (trackInfoList.get(i).speed == null ? "0" : trackInfoList.get(i).speed) + getString(R.string.unit);
                        viewModel.setWorkSpeed(speedStr);
                        break;
                    } else {
                        if (showLeftPosition > 0 && progress - showLeftPosition < offLineSecond) {
                            viewModel.showLeftLayout(true);
                        } else {
                            showLeftPosition = 0;
                            viewModel.showLeftLayout(false);
                        }
                    }
                }

            }
        }
    }

    private List<LatLng> points = new ArrayList<>();
    private List<LatLng> dragPoints = new ArrayList<>();

    private void calculateToMove(int progress) {
        int leftTime = 0;

        points.clear();
        dragPoints.clear();

        long dragStartPosition = 0;
        long dragEndPosition = progress;

//        for (int k = 0; k < playInfoList.size(); k++) {
//            if (progress < playInfoList.get(0).startPosition) {
//                break;
//            } else {
//                if (playInfoList.get(k).startPosition == progress) {
//                    currentIndex = k;
//                    points.addAll(playInfoList.get(k).playPoints);
//                    Log.d(TAG, "playPoints.size == >  " + points.size());
//
//                    if (SPEED_TYPE == Constant.FINALVALUE.SPEED_TIME_DOUBLE) {
//                        leftTime = playInfoList.get(k).allTime / 12;
//                    } else if (SPEED_TYPE == Constant.FINALVALUE.SPEED_TIME) {
//                        leftTime = playInfoList.get(k).allTime / 24;
//                    } else {
//                        leftTime = playInfoList.get(k).allTime / 48;
//                    }
//                    break;
//                } else {
//                    Log.d(TAG, "position == >  " + playInfoList.get(k).startPosition + " , progress=>" + progress);
//                }
//            }
//        }
        for (int k = 0; k < myPlayList.size(); k++) {
            if (progress < myPlayList.get(0).startPosition) {
                break;
            } else {
                if (myPlayList.get(k).startPosition == progress) {
//                    currentIndex = k;
                    points.addAll(myPlayList.get(k).points);

                    int deep = getCurDeep();
                    leftTime = (int) ((myPlayList.get(k).endPosition - myPlayList.get(k).startPosition) / deep);
//                    Log.d(TAG, "playPoints.size == >  " + points.size() + leftTime);
                    break;
                } else {
                    if (isDrag) {
//                        Log.d(TAG, "重置: startPosition =>" + myPlayList.get(k).startPosition + ", progress=>" + progress + ", endPosition:" + myPlayList.get(k).endPosition);

                        if (myPlayList.get(k).startPosition < progress && progress <= myPlayList.get(k).endPosition) {
                            dragStartPosition = myPlayList.get(k).startPosition;
                            dragEndPosition = myPlayList.get(k).endPosition;
                            dragPoints.addAll(myPlayList.get(k).points);
                            break;
                        }
                    }

//                    Log.d(TAG, "position == >  " + myPlayList.get(k).startPosition + " , progress=>" + progress + ", isDrag=>" + isDrag);
                }
            }
        }


        if (points.size() > 1 && mPlayState == 1) {

            dismissLoading();

            Log.d(TAG, "progress ==> " + progress + " , playPoints.size ==> " + points.size());
            // 取轨迹点的第一个点 作为 平滑移动的启动
            LatLng drivePoint = points.get(0);
            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
            points.set(pair.first, drivePoint);
            List<LatLng> subList = points.subList(pair.first, points.size());

            smoothMarker.setPoints(subList);
            smoothMarker.setTotalDuration(leftTime);
            smoothMarker.startSmoothMove();
        } else if (isDrag && mPlayState == 1) {
            isDrag = false;
            dismissLoading();

            long all = dragEndPosition - dragStartPosition;
            if (all > 0) {
                int subStart = (int) ((progress - dragStartPosition) * 1d / all * dragPoints.size());
                Log.d(TAG, "calculateToMove: subStart => " + subStart);

                //dragStartPosition =>0, progress=>77880, dragEndPosition:77880
//                Log.d(TAG, "重置: dragStartPosition =>" + dragStartPosition + ", progress=>" + progress + ", dragEndPosition:" + dragEndPosition);

                if (subStart > 0 && subStart < dragPoints.size()) {
                    List<LatLng> latLngs = dragPoints.subList(subStart, dragPoints.size());
                    LatLng drivePoint = latLngs.get(0);
                    Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(latLngs, drivePoint);
                    latLngs.set(pair.first, drivePoint);
                    List<LatLng> leftPoints = latLngs.subList(pair.first, latLngs.size());

                    int deep = getCurDeep();
                    leftTime = (int) ((dragEndPosition - progress) / deep);
//                    Log.d(TAG, "重置: dragEndPosition =>" + dragEndPosition + ", progress=>" + progress + ", size:" + precPoint.size());

                    smoothMarker.setPoints(leftPoints);
                    smoothMarker.setTotalDuration(leftTime);
                    smoothMarker.startSmoothMove();
                }
            }
        }

    }

    //获取计算时间的参数
    private int getCurDeep() {
        int deep;
        if (SPEED_TYPE == Constant.FINALVALUE.SPEED_TIME_DOUBLE) deep = 12;
        else if (SPEED_TYPE == Constant.FINALVALUE.SPEED_TIME) deep = 24;
        else if (SPEED_TYPE == Constant.FINALVALUE.SPEED_TIME_HALF) deep = 48;
        else deep = 96;
        return deep;
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

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearPlotMarkers(true);
        if (trackInfoList != null) trackInfoList.clear();
        if (mHandler != null) mHandler.removeMessages(1);
        if (bitmapDescriptor != null) bitmapDescriptor.recycle();
        if (smoothMarker != null) smoothMarker.removeMarker();
        if (binding.map != null) binding.map.onDestroy();
        if (binding != null) binding.unbind();
    }


    @Override
    protected boolean isShowBacking() {
        return true;
    }

    //轨迹 -> map -> 颜色线 working or running
    private void addColorLine(List<LatLng> points, String workType) {
        boolean isWork = Constant.FINALVALUE.WORKING.equals(workType);
        int color = getResources().getColor(isWork ? R.color.color_009688 : R.color.color_3F51B5);
        if (points != null && points.size() > MAX_LINE_POINT_SIZE) {
            List<List<LatLng>> groupList = MapUtils.groupList(points, MAX_LINE_POINT_SIZE);
            for (List<LatLng> latLngs : groupList) {
                drawColorPath(color, latLngs);
            }
        } else {
            drawColorPath(color, points);
        }
    }

    private void drawColorPath(int color, List<LatLng> latLngs) {
        Polyline polyline = aMap.addPolyline(polylineOptions);
        polyline.setColor(color);
        polyline.setPoints(latLngs);
    }


    //进度条 -> 颜色线
    private void addSeekBarColor(String workType, long from, long to) {
        binding.layoutPlayBackRightView.rightLayout.setVisibility(View.VISIBLE);
        ColorInfo colorInfo = new ColorInfo();
        switch (workType) {
            case Constant.FINALVALUE.WORKING:
                colorInfo.colorInt = getResources().getColor(R.color.color_009688);
                colorInfo.desc = Constant.FINALVALUE.WORKING;
                viewModel.showWorkLayout(true);
                break;
            case Constant.FINALVALUE.RUNNING:
                colorInfo.colorInt = getResources().getColor(R.color.color_3F51B5);
                colorInfo.desc = Constant.FINALVALUE.RUNNING;
                viewModel.showRunLayout(true);
                break;
            default:
                colorInfo.colorInt = getResources().getColor(R.color.grey);
                colorInfo.desc = Constant.FINALVALUE.OFF_LINE;
                viewModel.showOffLineLayout(true);
                break;
        }
        colorInfo.from = from;
        colorInfo.to = to;
        colorInfoList.add(colorInfo);
    }


    //开始移动
    public void startOrStopMenu(View view) {
        if (trackInfoList == null || trackInfoList.size() == 0) return;
        if (mPlayState == 0) {
            //若在暂停状态 开始移动
            if (binding.rangeSeekBar.getProgress() == RangeSeekBar.SCALE_MAX) {
                binding.rangeSeekBar.scrollerToProgress(0);
            }
            mPlayState = 1;
            isDrag = true;
            showLoading();
            mHandler.sendEmptyMessage(1);
        } else {
            // 暂停
            mPlayState = 0;
            mHandler.sendEmptyMessage(2);
        }
        binding.playBtn.setBackgroundResource(mPlayState == 1 ? R.drawable.icon_broadcast : R.drawable.icon_circleplay);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int progress = binding.rangeSeekBar.getProgress() + 1;
                if (progress < RangeSeekBar.SCALE_MAX) {
                    binding.rangeSeekBar.moveProgress(progress);
                }
                if (binding.rangeSeekBar.getProgress() < RangeSeekBar.SCALE_MAX) {
                    //根据定时时间设置速度
                    sendEmptyMessageDelayed(1, (long) SPEED_TYPE);
                }
            } else if (msg.what == 2) {
                removeMessages(1);
                if (smoothMarker != null) {
                    smoothMarker.stopMove();
                }
            }
        }
    };

    //设置速度弹出框
    public void handleSelectSpeed(View view) {
        if (!singleBottomPopWin.isShowing()) {
            singleBottomPopWin.show(binding.getRoot(), getWindow(), speedDefaultIndex, 1);
        }
    }


    //选择了倍速
    @Override
    public void onSingleBottomPopWinClick(int type, int index) {
        if (speedDefaultIndex != index) {
            isDrag = true;
            speedDefaultIndex = index;
            binding.speedTv.setText(speedList.get(index));
            if (index == 0) {
                SPEED_TYPE = Constant.FINALVALUE.SPEED_TIME_DOUBLE;
            } else if (index == 1) {
                // 24小时的轨迹24*60*60*1000
                SPEED_TYPE = Constant.FINALVALUE.SPEED_TIME;
            } else if (index == 2) {
                SPEED_TYPE = Constant.FINALVALUE.SPEED_TIME_HALF;
            } else {
                SPEED_TYPE = Constant.FINALVALUE.SPEED_TIME_FOUR_HALF;
            }

            if (smoothMarker != null) {
                smoothMarker.stopMove();
            }
            if (mPlayState == 1) {
                showLoading();
            }
        }
        singleBottomPopWin.dismiss();
    }


    //离线图标
    private void addOfflineMarker(LatLng latLng) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_img_marker, null);
        MarkerOptions options = new MarkerOptions().position(latLng).anchor(0f, 0.5f).icon(BitmapDescriptorFactory.fromView(view));
        aMap.addMarker(options).setZIndex(995);
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

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //地块中心marker显示
        Boolean isShowPlotCenter = cameraPosition.zoom >= SHOW_PLOT_CENTER_ZOOM;
        if (isShowPlotCenter != isActualShowCenter) {
            isActualShowCenter = isShowPlotCenter;
            Log.d(TAG, "onCameraChangeFinish: 地块中心：" + cameraPosition.zoom + " , " + isActualShowCenter);
            showPlotCenter(isActualShowCenter);
        }
    }

    private void showPlotCenter(boolean isShowCenter) {
        if (plotMarker.size() > 0) {
            try {
                Single.just(isShowCenter).observeOn(Schedulers.io()).doOnSubscribe(disposable -> {
                    try {
                        for (Marker marker : plotMarker) {
                            if (marker != null) marker.setVisible(isShowCenter);
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
