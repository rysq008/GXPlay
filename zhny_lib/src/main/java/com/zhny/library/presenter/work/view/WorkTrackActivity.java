package com.zhny.library.presenter.work.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlay;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.R;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.common.Constant;
import com.zhny.library.databinding.ActivityWorkTrackBinding;
import com.zhny.library.presenter.monitor.helper.MonitorHelper;
import com.zhny.library.presenter.work.WorkConstants;
import com.zhny.library.presenter.work.dialog.ViewPictureDialog;
import com.zhny.library.presenter.work.dto.PictureDto;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.dto.WorkTrackDto;
import com.zhny.library.presenter.work.listener.WorkTrackViewClickListener;
import com.zhny.library.presenter.work.viewmodel.WorkTrackViewModel;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class WorkTrackActivity extends BaseActivity implements WorkTrackViewClickListener,
        AMap.OnCameraChangeListener, AMap.OnMultiPointClickListener {

    private WorkTrackViewModel viewModel;
    private ActivityWorkTrackBinding binding;
    private AMap mAMap;

    private String fieldName;
    private String fieldCode;
    private String fieldCenter;
    private String fieldCoordinates;
    private WorkDto workDto;

    private Integer standardValue, floatValue, invalidValue;
    private Boolean openPicture, openWidth;

    private Polygon polygon;
    private Marker plotMarker;

    private List<Polyline> widthPolylineList = new CopyOnWriteArrayList<>();
    private List<Polyline> trackPolylineList = new CopyOnWriteArrayList<>();
    boolean isPloughing;

    private BitmapDescriptor picBitmapDescriptor;

    private ViewPictureDialog viewPictureDialog;
    List<LatLng> allLatLngs = new CopyOnWriteArrayList<>();

    private HashMap<Polyline, Double> hashMap = new HashMap<>();
    //海量点的绘制
    MultiPointOverlayOptions overlayOptions;
    MultiPointOverlay lightGreenMultiPointOverlay;
    List<MultiPointItem> lightGreenList = new ArrayList<>();


    MultiPointOverlay greenMultiPointOverlay;
    List<MultiPointItem> greenList = new ArrayList<>();

    MultiPointOverlay redMultiPointOverlay;
    List<MultiPointItem> redList = new ArrayList<>();

    MultiPointOverlay picMultiPointOverlay;
    List<MultiPointItem> picList = new ArrayList<>();
    List<String> imgUrls = new ArrayList<>();

    private List<WorkTrackDto> workTrackDtos = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>();

    private TileOverlay tileOverlay;
    private List<Integer> showDeepWorkType = new ArrayList<>(); //记录显示作业质量深度的作业类型

    private PolylineOptions polylineOptions, widthPolylineOptions;
    // addPolyline 绘制上限
    private static final int MAX_LINE_POINT_SIZE = 5000;


    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            fieldName = params.getString(WorkConstants.BUNDLE_FIELD_NAME);
            fieldCode = params.getString(WorkConstants.BUNDLE_FIELD_CODE);
            fieldCenter = params.getString(WorkConstants.BUNDLE_FIELD_CENTER);
            fieldCoordinates = params.getString(WorkConstants.BUNDLE_FIELD_COORDINATES);
            Serializable workDto = params.getSerializable(WorkConstants.BUNDLE_WORKDTO);

            if (workDto instanceof WorkDto) {
                this.workDto = (WorkDto) workDto;
            }
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(WorkTrackViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_track);
        binding.setViewModel(viewModel);
        binding.layoutWorkTrackLeftView.setViewModel(viewModel);
        binding.layoutWorkTrackRightView.setViewModel(viewModel);

        binding.map.onCreate(savedInstanceState);
        mAMap = binding.map.getMap();
        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        mAMap.getUiSettings().setScaleControlsEnabled(true);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setOnMultiPointClickListener(this);
        mAMap.setOnCameraChangeListener(this);
        viewPictureDialog = new ViewPictureDialog();
        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(fieldName);

        if (tileOverlay != null)  tileOverlay.remove();
        tileOverlay = MapUtils.addRemoteOverlay(mAMap);

        //right view
        binding.layoutWorkTrackRightView.setClickListener(this);
        binding.setClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handlerDeepWorkType();
        initData();
        initMultiPointOverlay();
        drawLand(fieldCoordinates, fieldCenter);
        if (workTrackDtos.size() == 0) {
            getWorkTrackData();
        } else {
            refreshView();
        }
    }

    //处理快码 显示作业质量深度的作业类型
    private void handlerDeepWorkType() {
        try {
            showDeepWorkType.clear();
            String codeStr = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.DEEP_OF_WORK_TYPE_CODE, Constant.FINALVALUE.DEEP_OF_WORK_TYPE);
            String[] codeStrList = codeStr.split(",");
            for (String code : codeStrList) {
                showDeepWorkType.add(Integer.valueOf(code));
            }
            Log.d(TAG, "handlerDeepWorkType: 请求快码 DEEP_OF_WORK_TYPE_CODE ==> " + showDeepWorkType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMultiPointOverlay() {
        overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromView(getView(0)));//设置图标
        overlayOptions.anchor(0.5f, 0.5f); //设置锚点
        if (lightGreenMultiPointOverlay != null) lightGreenMultiPointOverlay.remove();
        lightGreenMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);

        overlayOptions.icon(BitmapDescriptorFactory.fromView(getView(1)));//设置图标
        if (greenMultiPointOverlay != null) greenMultiPointOverlay.remove();
        greenMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);


        overlayOptions.icon(BitmapDescriptorFactory.fromView(getView(2)));//设置图标
        if (redMultiPointOverlay != null) redMultiPointOverlay.remove();
        redMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);

        View picView = LayoutInflater.from(this).inflate(R.layout.maker_pic_image, null);
        ((ImageView) picView.findViewById(R.id.iv_pic_image)).setImageResource(R.drawable.icon_img);
        picBitmapDescriptor = BitmapDescriptorFactory.fromView(picView);
        overlayOptions.icon(picBitmapDescriptor);
        if (picMultiPointOverlay != null) picMultiPointOverlay.remove();
        picMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);


        polylineOptions = new PolylineOptions()
                .width(DisplayUtils.dp2px(1.0f))
                .color(Color.parseColor("#009688")).zIndex(999);

        widthPolylineOptions = new PolylineOptions()
                .width(DisplayUtils.dp2px(5.2f))
                .color(Color.parseColor("#52999999")).zIndex(999);

    }


    private void initData() {
        openPicture = SPUtils.getInstance(Constant.SP.SP_NAME).getBoolean(Constant.SP.WORK_TRACK_OPEN_PICTURE, Constant.SP.WORK_TRACK_OPEN_PICTURE_DEFAULT);
        viewModel.showPicture.setValue(openPicture);
        openWidth = SPUtils.getInstance(Constant.SP.SP_NAME).getBoolean(Constant.SP.WORK_TRACK_OPEN_WIDTH, Constant.SP.WORK_TRACK_OPEN_WIDTH_DEFAULT);
        viewModel.showWidth.setValue(openWidth);


        //left view
        if (workDto != null) {
            viewModel.jobType.setValue(workDto.jobTypeMeaning);
            isPloughing =  showDeepWorkType.contains(workDto.jobType);
            viewModel.isPloughing.setValue(isPloughing);
            if (isPloughing) {
                showSetting();
                standardValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_TRACK_SETTING_PLOWING_STANDARD_VALUE
                        , Constant.SP.WORK_TRACK_SETTING_PLOWING_STANDARD);

                floatValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_TRACK_SETTING_PLOWING_FLOAT_VALUE
                        , Constant.SP.WORK_TRACK_SETTING_PLOWING_FLOAT);

                invalidValue = SPUtils.getInstance(Constant.SP.SP_NAME).getInt(Constant.SP.WORK_INVALID_VALUE
                        , Constant.SP.WORK_INVALID);

                viewModel.standardValue.setValue(standardValue + getString(R.string.company_cm));
                viewModel.floatValue.setValue(floatValue + getString(R.string.company_cm));

            } else {
                viewModel.width.setValue(workDto.width + getString(R.string.company_cm));
            }
        }

    }

    private void drawLand(String fieldCoordinates, String fieldCenter) {
        //绘制地块路径
        latLngs.clear();
        latLngs = MonitorHelper.getLatLngs(fieldCoordinates);
        if (latLngs == null || latLngs.size() == 0) return;
        if (polygon == null) {
            PolygonOptions polygonOptions = new PolygonOptions()
                    .strokeWidth(DisplayUtils.dp2px(1f))
                    .strokeColor(Color.parseColor("#FFB100"))
                    .fillColor(Color.parseColor("#00000000"))
                    .addAll(latLngs);
            polygon = mAMap.addPolygon(polygonOptions);
            polygon.setZIndex(999);

        } else {
            polygon.setPoints(latLngs);
        }


        //绘制地块marker
        if (!TextUtils.isEmpty(fieldCenter)) {
            try {
                String[] str = fieldCenter.split(",");
                LatLng latLng = new LatLng(Double.valueOf(str[1]), Double.valueOf(str[0]));
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromView(getPlotView(fieldName)));

                if (plotMarker == null) {
                    plotMarker = mAMap.addMarker(options);
                } else {
                    plotMarker.setMarkerOptions(options);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        movePoints(latLngs);
    }


    private void getWorkTrackData() {
        //请求作业轨迹数据
        if (workDto == null || TextUtils.isEmpty(fieldCode) || TextUtils.isEmpty(workDto.startTime) || TextUtils.isEmpty(workDto.endTime))
            return;
        showLoading();
        viewModel.getWorkTrack(
                SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID),
                fieldCode, workDto.jobType + "", workDto.startTime, workDto.endTime)
                .observe(this, baseDto -> {
                    workTrackDtos.clear();
                    workTrackDtos.addAll(baseDto.getContent());
                    refreshView();
                    dismissLoading();
                });

    }

    private void refreshView() {
        Log.d(TAG, "refreshView: dismissLoading ===>" );
        if (workTrackDtos.size() > 0) {
            allLatLngs.clear();
            hashMap.clear();
            drawWorkTrack(workTrackDtos);
        }
    }


    private void getPictureData() {
        //请求作业轨迹 图片 数据
        if (workDto == null || TextUtils.isEmpty(fieldCode) || TextUtils.isEmpty(workDto.sn)
                || TextUtils.isEmpty(workDto.startTime) || TextUtils.isEmpty(workDto.endTime))
            return;
        viewModel.getPictureData(fieldCode, workDto.sn, workDto.startTime, workDto.endTime, workDto.jobType + "")
                .observe(this, baseDto -> {
                    List<PictureDto> pictureDtos = baseDto.getContent();
                    if (pictureDtos != null && pictureDtos.size() > 0) {
                        showPicture(pictureDtos);
                    }
                });
    }


    private View getPlotView(String plotName) {
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_plot_marker, null);
        view.setText(plotName);
        return view;
    }


    private void drawWorkTrack(List<WorkTrackDto> trackDtos) {

        if (widthPolylineList != null && widthPolylineList.size() > 0) {
            for (int i = 0; i < widthPolylineList.size(); i++) {
                widthPolylineList.get(i).remove();
                trackPolylineList.get(i).remove();
            }
            widthPolylineList.clear();
            trackPolylineList.clear();
        }


        int totalNum = 0;//总数
        int qualifiedNum = 0;//合格数
        lightGreenList.clear();
        greenList.clear();
        redList.clear();


        //绘制轨迹

        for (WorkTrackDto workTrackDto : trackDtos) {
            List<WorkTrackDto.TrackListBean> trackPoints = workTrackDto.trackList;
            List<LatLng> latLngs = new ArrayList<>();
            if (trackPoints != null && trackPoints.size() > 0) {
                for (WorkTrackDto.TrackListBean trackPoint : trackPoints) {
                    LatLng latLng = new LatLng(trackPoint.latitude, trackPoint.longitude);
                    latLngs.add(latLng);
                    double depth = trackPoint.depth;
                    if (isPloughing && depth > invalidValue) { //无效值不参与计算
                        //创建MultiPointItem存放，海量点中某单个点的位置及其他信息
                        MultiPointItem multiPointItem = new MultiPointItem(latLng);
                        totalNum++;
                        if (depth >= standardValue) {
                            qualifiedNum++;
                            lightGreenList.add(multiPointItem);
                        } else if (depth > standardValue - floatValue) {
                            qualifiedNum++;
                            greenList.add(multiPointItem);
                        } else {
                            redList.add(multiPointItem);
                        }
                    }
                }

                if (latLngs.size() > MAX_LINE_POINT_SIZE) {
                    List<List<LatLng>> groupList = MapUtils.groupList(latLngs, MAX_LINE_POINT_SIZE);
                    for (List<LatLng> points : groupList) {
                        drawColorPath(workTrackDto, points);
                    }
                } else {
                    drawColorPath(workTrackDto, latLngs);
                }

                allLatLngs.addAll(latLngs);
            }
        }

        lightGreenMultiPointOverlay.setItems(lightGreenList);
        greenMultiPointOverlay.setItems(greenList);
        redMultiPointOverlay.setItems(redList);

        //playPoints(allLatLngs);


        getPictureData();

        //计算合格率
        if (isPloughing) {
            String accuracyRate = "0.00";
            if (totalNum != 0) {
                double one = Double.parseDouble(qualifiedNum + "");
                double two = Double.parseDouble(totalNum + "");
                Log.d(TAG, "one:" + one + ",tow:" + two);
                double percent = one / two;
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(2);
                accuracyRate = nt.format(percent);
            }
            viewModel.accuracyRate.setValue(accuracyRate);
        }
    }

    private void drawColorPath(WorkTrackDto workTrackDto, List<LatLng> points) {
        Polyline widthPolyline = mAMap.addPolyline(widthPolylineOptions);
        widthPolyline.setPoints(points);
        widthPolyline.setVisible(openWidth);
        widthPolylineList.add(widthPolyline);
        hashMap.put(widthPolyline, workTrackDto.width);

        Polyline polyline = mAMap.addPolyline(polylineOptions);
        polyline.setPoints(points);
        trackPolylineList.add(polyline);
    }


    private ImageView getView(int type) {
        ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_track_marker, null);
        switch (type) {
            case 0: view.setBackgroundResource(R.drawable.shape_track_point_light_green); break;
            case 1: view.setBackgroundResource(R.drawable.shape_track_point_green); break;
            case 2:  view.setBackgroundResource(R.drawable.shape_track_point_red); break;
        }
        return view;
    }


    //移动到聚合点
    private void movePoints(List<LatLng> latLngs) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            boundsBuilder.include(latLng);
        }
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), DisplayUtils.dp2px(19.2f)));
    }


    private void showPicture(List<PictureDto> pictureDtos) {
        picList.clear();
        imgUrls.clear();
        int picNum = 0;
        for (PictureDto pictureDto : pictureDtos) {
            List<PictureDto.ImgBean> imgs = pictureDto.imgs;
            if (imgs != null && imgs.size() > 0) {
                for (PictureDto.ImgBean img : imgs) {
                    Log.d(TAG, "img: url" + img.url + ",纬度：" + img.latitude + ",经度：" + img.longitude);
                    LatLng latLng = new LatLng(img.latitude, img.longitude);
                    MultiPointItem multiPointItem = new MultiPointItem(latLng);
                    multiPointItem.setCustomerId(WorkConstants.Pic_MultiPointItem_Custom_Id + picNum);
                    picNum++;
                    picList.add(multiPointItem);
                    imgUrls.add(img.url);
                }
            }
        }
        picMultiPointOverlay.setItems(picList);
        picMultiPointOverlay.setEnable(openPicture);
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
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
        if (picBitmapDescriptor != null) picBitmapDescriptor.recycle();
        if (binding != null) binding.unbind();
    }


    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected void onSettingListener() {
        startActivity(TrackSettingActivity.class);
    }


    /**
     * 图片开关
     */
    @Override
    public void onSwitchPicture() {
        openPicture = !openPicture;
        viewModel.showPicture.setValue(openPicture);
        //保存数据到sp
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_OPEN_PICTURE, openPicture);

        if (picMultiPointOverlay == null) return;
        picMultiPointOverlay.setEnable(openPicture);
    }

    /**
     * 幅宽开关
     */
    @Override
    public void onSwitchWidth() {
        openWidth = !openWidth;
        viewModel.showWidth.setValue(openWidth);
        SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.WORK_TRACK_OPEN_WIDTH, openWidth);

        if (widthPolylineList == null || widthPolylineList.size() == 0) return;
        for (Polyline p : widthPolylineList) {
            p.setVisible(openWidth);
        }
    }


    //回复页面
    public void clickReBackView(View view) {
        movePoints(latLngs);
    }


    @Override
    public void onZoom(boolean isZoomIn) {
        if (isZoomIn) {
            CameraUpdate update = CameraUpdateFactory.zoomIn();
            mAMap.animateCamera(update);
        } else {
            CameraUpdate update = CameraUpdateFactory.zoomOut();
            mAMap.animateCamera(update);

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
        float scalePerPixel = mAMap.getScalePerPixel();
        Log.d(TAG, "scalePerPixel:" + scalePerPixel);
        float d = 1 / scalePerPixel;
        if (widthPolylineList == null || widthPolylineList.size() == 0) return;
        for (Polyline p : widthPolylineList) {
            Double width = hashMap.get(p);
            if (width != null) {
                float result = (float) (d * width / 100);
                //result为幅宽实际在地图中的宽度,DisplayUtils.dp2px(1.0f)为轨迹宽度
                p.setWidth(result + DisplayUtils.dp2px(1.0f) * 1.2f);
            }
        }
    }

    @Override
    public boolean onPointClick(MultiPointItem selectItem) {
        if (openPicture  && !picList.isEmpty() && !imgUrls.isEmpty()) {
            LatLng latLng = selectItem.getLatLng();
            for (int i = 0; i < picList.size(); i++) {
                LatLng item = picList.get(i).getLatLng();
                if (item.longitude == latLng.longitude && item.latitude == latLng.latitude) {
                    viewPictureDialog.setParams(i, imgUrls);
                    viewPictureDialog.show(getSupportFragmentManager(), null);
                    break;
                }
            }
        }
        return true;
    }
}

