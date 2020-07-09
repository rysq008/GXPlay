package com.zhny.zhny_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.rance.library.ButtonData;
import com.rance.library.ButtonEventListener;
import com.rance.library.SectorMenuButton;
import com.sinochem.map.impl.AmapView;
import com.sinochem.map.observer.IMapMarkerClickObserver;
import com.sinochem.map.observer.IMapPOIClickObserver;
import com.zhny.library.presenter.fence.view.FenceActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DrawTileActivity extends AppCompatActivity implements View.OnClickListener {
    AmapView amapView;
//    private TextureMapView mapView;

//    private AMap aMap;

    //存放所有marker点标记

    private List<MyLatLng> allLatLngs = new ArrayList<>();

    //所有线段的点

    private List<LatLng> allLatLngsWithLine = new ArrayList<>();

    //所有marker点

    private List<Marker> markers = new ArrayList<>();

    //线的对象

    private Polyline polyline;

    //多边形的对象

    private Polygon polygon;

    //是否为封闭图形

    private boolean isEnd = false;

    //矩形对象的配置

    private PolygonOptions polygonOptions = new PolygonOptions();

    //当前触摸的marker

    private Marker touchMark;

    //当前选中的点下标,相对于allLatLngs的

    private int checkPos;

    //地图设置

    UiSettings uiSettings;

    ///周长

    private TextView mPerimeterTv;

    //面积

    private TextView mAreaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawtile);
        findViewById(R.id.drawtile_menu_iv).setOnClickListener(this);
        initRightBottomSectorMenuButton();
        initRightTopSectorMenuButton();
        initBottomCenterSectorMenuButton();
        mPerimeterTv = findViewById(R.id.drawtile_circle_tv);
        mAreaTv=findViewById(R.id.drawtile_area_tv);

        amapView = findViewById(R.id.drawtile_amapView);
        amapView.onCreate(savedInstanceState);
//        amapView.getMapManager().setMapTouchEventDispatcher(new IMapTouchEventDispatcher() {
//            @Override
//            public void dispatchTouchEvent(MotionEvent event) {
//                amapView.getMapManager().getMapTouchEventDispatcher().dispatchTouchEvent(event);
//            }
//        });

        amapView.getMapManager().setClampMapOnMarkerClick(true);
        amapView.getMapManager().setBringMarkerToFrontOnItClick(true);

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(createRectangle(new LatLng(31.238068, 121.501654), 0.25, 0.25));

        amapView.getMapManager().getMapFunctions().addPolygon(polygonOptions);

        ImageView fabContent = new ImageView(getActivity());
        fabContent.setImageDrawable(getResources().getDrawable(R.drawable.icon_zoom));

//        FloatingActionButton darkButton = new FloatingActionButton.Builder(getActivity())
//                .setTheme(FloatingActionButton.THEME_LIGHT)
//                .setContentView(fabContent)
//                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
//                .build();
//
//        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(getActivity()).setTheme(SubActionButton.THEME_LIGHT);
//        TextView rlIcon1 = new TextView(getActivity());rlIcon1.setText("清空");
//        TextView rlIcon2 = new TextView(getActivity());rlIcon2.setText("打点");
//        TextView rlIcon3 = new TextView(getActivity());rlIcon3.setText("撤回");
//
//        rlIcon1.setCompoundDrawablesRelativeWithIntrinsicBounds(0,(R.drawable.amap_bus),0,0);
//        rlIcon2.setCompoundDrawablesRelativeWithIntrinsicBounds(0,(R.drawable.amap_car),0,0);
//        rlIcon3.setCompoundDrawablesRelativeWithIntrinsicBounds(0,(R.drawable.amap_man),0,0);
//        int w = getResources().getDimensionPixelSize(R.dimen.dp_150);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(w,w);
//        rLSubBuilder.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        // Set 4 SubActionButtons
//        FloatingActionMenu centerBottomMenu = new FloatingActionMenu.Builder(getActivity())
//                .setStartAngle(-20)
//                .setEndAngle(-160)
//                .setRadius(getResources().getDimensionPixelSize(R.dimen.dp_150))
//                .setAnimationHandler(new SlideInAnimationHandler())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon1,layoutParams).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon2,layoutParams).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon3,layoutParams).build())
//                .attachTo(darkButton)
//                .build();
//        centerBottomMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
//            @Override
//            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
//                fabContent.setRotation(0);
//                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabContent,PropertyValuesHolder.ofFloat(View.ROTATION,45));
//                animator.start();
//            }
//
//            @Override
//            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
//                fabContent.setRotation(45);
//                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fabContent,PropertyValuesHolder.ofFloat(View.ROTATION,0));
//                animator.start();
//            }
//        });

        init();
    }

    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
        latLngs.add(new LatLng(center.latitude - halfHeight, center.longitude + halfWidth));
        latLngs.add(new LatLng(center.latitude + halfHeight, center.longitude + halfWidth));
        latLngs.add(new LatLng(center.latitude + halfHeight, center.longitude - halfWidth));
        return latLngs;
    }


    private Activity getActivity() {
        return this;
    }

    private void initRightTopSectorMenuButton() {
        SectorMenuButton sectorMenuButton = (SectorMenuButton) findViewById(R.id.drawtile_menu_iv);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.mipmap.like, R.mipmap.mark, R.mipmap.search, R.mipmap.copy};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            buttonData.setBackgroundColorId(this, R.color.colorAccent);
            buttonDatas.add(buttonData);
        }
        sectorMenuButton.setButtonDatas(buttonDatas);
        setListener(sectorMenuButton);
    }

    private void initRightBottomSectorMenuButton() {
        SectorMenuButton sectorMenuButton = (SectorMenuButton) findViewById(R.id.drawtile_local_iv);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.mipmap.like, R.mipmap.mark, R.mipmap.search, R.mipmap.copy};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            buttonData.setBackgroundColorId(this, R.color.colorAccent);
            buttonDatas.add(buttonData);
        }
        sectorMenuButton.setButtonDatas(buttonDatas);
        setListener(sectorMenuButton);
    }

    private void initBottomCenterSectorMenuButton() {
        SectorMenuButton sectorMenuButton = (SectorMenuButton) findViewById(R.id.drawtile_bottom_center);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.icon_zoom,R.drawable.amap_bus, R.drawable.amap_car, R.drawable.amap_man};
        String[] strings = {"","清空","打点","撤回"};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            buttonData.setBackgroundColorId(this, R.color.white);
            buttonData.setText(strings[i]);
            buttonDatas.add(buttonData);
        }
        sectorMenuButton.setButtonDatas(buttonDatas);
        setListener(sectorMenuButton);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawtile_menu_iv:
                startActivity(new Intent(this, SystemOverlayMenuService.class));
                break;
        }
    }

    private void setListener(final SectorMenuButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                startActivity(new Intent(DrawTileActivity.this, FenceActivity.class));
                showToast("button" + index);
            }

            @Override
            public void onExpand() {
                showToast("onExpand");
            }

            @Override
            public void onCollapse() {
                showToast("onCollapse");
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void showDialog(){
//        DialogFragmentHelper.showBuilderDialog(getSupportFragmentManager(),
//                DialogFragmentHelper.builder(R.layout.dialog_delete_driver, true)
//                        .setDialogWindow(dialogWindow -> {
//                            WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
//                            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                            dialogWindow.setAttributes(wlp);
//                            dialogWindow.setGravity(Gravity.BOTTOM);
//                            dialogWindow.setWindowAnimations(android.R.style.Animation_InputMethod);
//                            return null;
//                        }),"");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        amapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        amapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        amapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        amapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        amapView.onLowMemory();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
//        aMap = amapView.getMapManager().getMapFunctions();
        uiSettings = amapView.getMapManager().getMapFunctions().getUiSettings();
        //地图点击监听
        amapView.getMapManager().addObserver((IMapPOIClickObserver) poi -> {
            if(!isEnd){
                addMarker(poi.getCoordinate());
            }
            return true;
        });
        //marker点击监听
        amapView.getMapManager().addObserver(new IMapMarkerClickObserver() {
            @Override
            public boolean onMarkerClick(MotionEvent event, Marker marker) {
                if (isEnd) {
                } else {
                    if (markers.get(0).equals(marker)) {
                        //封闭图形
                        isEnd = true;
                        //初始化多边形参数
                        createAreaStyle();
                        //添加marker
                        addMarker(marker.getPosition());
                        //画多边形
                        drawRect();
                        return true;
                    } else {
                        //未封闭,点击其它marke
                    }
                }
                return false;
            }
        });

//        //添加拖拽事件
//        amapView.getMapManager().addObserver(new IMapTouchObserver() {
//            @Override
//            public boolean onMapTouch(MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    //按下
//                    case MotionEvent.ACTION_DOWN:
//                        float down_x= motionEvent.getX();
//                        float down_y= motionEvent.getY();
//                        Point downPoint= new Point();
//                        downPoint.set((int) down_x, (int) down_y);
//
//                        //获取触摸到的位置
//                        LatLng downLatLng= amapView.getMapManager().getMapFunctions().getProjection().fromScreenLocation(downPoint);
//
//                        //获取触摸的点下标
//                        checkPos = getNearestLatLng(downLatLng);
//
//                        //触摸的点是矩形的点
//                        if (checkPos > -1) {
//
//                            //如果是小点
//                            if (allLatLngs.get(checkPos).getState() == MyLatLng.UNABLE) {
//
//                                //更改为大点
//                                chageMarker();
//
//                                //如果是封闭图形并且点击的是最后的一个点
//                                if (isEnd && checkPos == allLatLngs.size() - 1) {
//
//                                    //添加两个mark
//                                    addTwoMarker(getCenterLatlng(allLatLngs.get(checkPos).getLatLng(), allLatLngs.get(checkPos - 1).getLatLng()), getCenterLatlng(allLatLngs.get(checkPos).getLatLng(), allLatLngs.get(0).getLatLng()));
//                                } else {
//
//                                    //不是封闭图形
//                                    //添加两个marker
//                                    addTwoMarker(getCenterLatlng(allLatLngs.get(checkPos).getLatLng(), allLatLngs.get(checkPos - 1).getLatLng()), getCenterLatlng(allLatLngs.get(checkPos).getLatLng(), allLatLngs.get(checkPos + 1).getLatLng()));
//                                }
//
//                                //将选中点的下标更改
//                                checkPos += 1;
//                            }
//
//                            //获取选中的marker点
//                            touchMark = markers.get(checkPos);
//
//                            //禁止地图放大旋转等操作
//                            uiSettings.setScrollGesturesEnabled(false);
//                        }
//                        break;
//                    //移动中
//                    case MotionEvent.ACTION_MOVE:
//                        //有选中的marker点
//                        if (touchMark != null) {
//                            float move_x= motionEvent.getX();
//                            float move_y= motionEvent.getY();
//                            Point movePoint= new Point();
//                            movePoint.set((int) move_x, (int) move_y);
//
//                            //获取到触摸点经纬度
//                            LatLng moveLatLng= amapView.getMapManager().getMapFunctions().getProjection().fromScreenLocation(movePoint);
//
//                            //更新的坐标点位置
//                            touchMark.setPosition(moveLatLng);
//
//                            //如果已经画出线(两个大点)
//                            if (polyline != null) {
//
//                                //会比markers多一个点
//                                List<LatLng> points= polyline.getPoints();
//
//                                //修改线数据中当前触摸点的坐标信息
//                                points.set(checkPos, moveLatLng);
//
//                                //修改当前选中marker点坐标集合的信息
//                                allLatLngs.get(checkPos).setLatLng(moveLatLng);
//
//                                //修改当前选中线的点的坐标集合的信息
//                                allLatLngsWithLine.set(checkPos, moveLatLng);
//
//                                //不需要添加两个点
//                                if (checkPos == 0) {
//
//                                    //获取选中大点旁边的小点
//                                    Marker marker= markers.get(checkPos + 1);
//
//                                    //获取第一个大点和第二个大点的中间坐标
//                                    LatLng center= getCenterLatlng(moveLatLng, allLatLngs.get(checkPos + 2).getLatLng());
//
//                                    //修改marker的坐标
//                                    marker.setPosition(center);
//
//                                    //修改线的坐标
//                                    points.set(checkPos + 1, center);
//
//                                    //修改总marker坐标集合信息
//                                    allLatLngs.get(checkPos + 1).setLatLng(center);
//
//                                    //修改总线集合信息
//                                    allLatLngsWithLine.set(checkPos + 1, center);
//
//                                    //如果是已经封闭的则需要修改最后一个大点与第一个大点中间点的坐标
//                                    if (isEnd) {
//
//                                        //操作同上
//                                        Marker marker2= markers.get(markers.size() - 1);
//
//                                        LatLng cen= getCenterLatlng(moveLatLng, allLatLngs.get(markers.size() - 2).getLatLng());
//
//                                        marker2.setPosition(cen);
//
//                                        points.set(points.size() - 1, moveLatLng);
//
//                                        points.set(points.size() - 2, cen);
//
//                                        allLatLngs.get(markers.size() - 1).setLatLng(cen);
//
//                                        allLatLngsWithLine.set(markers.size() - 1, cen);
//
//                                    }
//
//                                    //当触摸的点是最后一个大点或者最后一个小点的时候
//                                } else if (checkPos == markers.size() - 2 || checkPos == markers.size() - 1) {
//
//                                    //原理同上//最后一个点
//                                    Marker marker2= markers.get(checkPos - 1);
//                                    LatLng center= getCenterLatlng(moveLatLng, allLatLngs.get(checkPos - 2).getLatLng());
//                                    marker2.setPosition(center);
//                                    points.set(checkPos - 1, center);
//                                    allLatLngs.get(checkPos - 1).setLatLng(center);
//                                    allLatLngsWithLine.set(checkPos - 1, center);
//
//                                    if (isEnd) {
//
//                                        Marker marker= markers.get(checkPos + 1);
//
//                                        LatLng cen= getCenterLatlng(moveLatLng, allLatLngs.get(0).getLatLng());
//
//                                        marker.setPosition(cen);
//
//                                        points.set(checkPos + 1, cen);
//
//                                        allLatLngs.get(checkPos + 1).setLatLng(cen);
//
//                                        allLatLngsWithLine.set(checkPos + 1, cen);
//
//                                    }
//
//                                } else {
//
//                                    //原理同上//中间的点
//                                    Marker marker= markers.get(checkPos + 1);
//                                    LatLng center= getCenterLatlng(moveLatLng, allLatLngs.get(checkPos + 2).getLatLng());
//                                    marker.setPosition(center);
//                                    allLatLngs.get(checkPos + 1).setLatLng(center);
//                                    allLatLngsWithLine.set(checkPos + 1, center);
//                                    Marker marker2= markers.get(checkPos - 1);
//                                    LatLng center2= getCenterLatlng(moveLatLng, allLatLngs.get(checkPos - 2).getLatLng());
//                                    marker2.setPosition(center2);
//                                    allLatLngs.get(checkPos - 1).setLatLng(center2);
//                                    allLatLngsWithLine.set(checkPos - 1, center2);
//
//                                    //移动线
//                                    points.set(checkPos + 1, center);
//                                    points.set(checkPos - 1, center2);
//                                }
//
//                                //更改线数据
//                                polyline.setPoints(points);
//
//                                //计算周长
//                                setPerimeter();
//
//                                //如果封闭的话
//                                if (isEnd) {
//                                    //计算面积
//                                    drawRect();
//                                }
//                            }
//                        }
//                        break;
//                    //抬起
//                    case MotionEvent.ACTION_UP:
//                        if (touchMark != null) {
//                            //清除选中点信息
//                            touchMark = null;
//                            //恢复地图操作
//                            uiSettings.setScrollGesturesEnabled(true);
//                        }
//                        break;
//                }
//                return false;
//            }
//
//        });

    }

    /**
     * 获取两个点的中点坐标
     *
     * @parammyLatLng
     * @parammyLatLng2
     * @return
     */

    private LatLng getCenterLatlng(LatLng myLatLng, LatLng myLatLng2) {
        return new LatLng((myLatLng.latitude + myLatLng2.latitude) / 2, (myLatLng.longitude + myLatLng2.longitude) / 2);
    }

    /**
     * 修改makeer类型和icon
     */
    private void chageMarker() {
        Marker marker= markers.get(checkPos);
        //修改点类型为大点
        allLatLngs.get(checkPos).setState(MyLatLng.ABLE);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark));
    }

    /**
     * 在拖拽点两侧添加maker
     */
    private void addTwoMarker(LatLng latLng, LatLng latLng2) {
        List<LatLng> points= polyline.getPoints();
        //先添加2再添加1
        MarkerOptions options= new MarkerOptions();
        options.position(latLng2).draggable(false).visible(true);
        Marker marker= amapView.getMapManager().getMapFunctions().addMarker(options);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_multi_point_plot));
        //设置偏移
        marker.setAnchor(0.5f, 0.5f);
        //如果是最后一个点
        if (isEnd && checkPos == allLatLngs.size() - 1) {
            //直接把点信息添加进去
            allLatLngs.add(new MyLatLng(latLng2, MyLatLng.UNABLE));
            allLatLngsWithLine.add(latLng2);
            markers.add(marker);
        } else {
            //按位置插入点信息
            allLatLngs.add(checkPos + 1, new MyLatLng(latLng2, MyLatLng.UNABLE));
            allLatLngsWithLine.add(checkPos + 1, latLng2);
            markers.add(checkPos + 1, marker);
        }
        points.add(checkPos + 1, latLng2);
        MarkerOptions options2= new MarkerOptions();
        options2.position(latLng).draggable(false).visible(true);
        Marker marker2= amapView.getMapManager().getMapFunctions().addMarker(options2);
        marker2.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_multi_point_plot));
        marker2.setAnchor(0.5f, 0.5f);
        allLatLngs.add(checkPos, new MyLatLng(latLng, MyLatLng.UNABLE));
        allLatLngsWithLine.add(checkPos, latLng);
        markers.add(checkPos, marker2);
        points.add(checkPos, latLng);
        polyline.setPoints(points);
    }


    /**
     * 添加marker
     */
    private void addMarker(LatLng latLng) {
        if (allLatLngs.size() == 0) {
            MarkerOptions options= new MarkerOptions();
            options.position(latLng).draggable(false).visible(true);
            Marker marker= amapView.getMapManager().getMapFunctions().addMarker(options);
            marker.setObject((allLatLngs.size() + 1));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark));
            marker.setAnchor(0.5f, 0.5f);
            allLatLngs.add(new MyLatLng(latLng, MyLatLng.ABLE));
            allLatLngsWithLine.add(latLng);
            markers.add(marker);
            //画线
            drawLine(latLng, null);
        } else {
            if (isEnd) {
                MarkerOptions options= new MarkerOptions();
                latLng= new LatLng(((latLng.latitude + allLatLngs.get(allLatLngs.size() - 1).getLatLng().latitude) / 2), ((latLng.longitude + allLatLngs.get(allLatLngs.size() - 1).getLatLng().longitude) / 2));
                options.position(latLng).draggable(false).visible(true);
                Marker marker= amapView.getMapManager().getMapFunctions().addMarker(options);
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_multi_point_plot));
                marker.setAnchor(0.5f, 0.5f);
                allLatLngs.add(new MyLatLng(latLng, MyLatLng.UNABLE));
                allLatLngsWithLine.add(latLng);
                markers.add(marker);
                drawLine(allLatLngsWithLine.get(allLatLngsWithLine.size() - 1), allLatLngsWithLine.get(0));
            } else {
                for (int i= 0; i< 2; i++) { // 在地图上添一组图片标记（marker）对象，并设置是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示
                    MarkerOptions options= new MarkerOptions();
                    if (i== 0) {
                        LatLng latLng2= new LatLng(((latLng.latitude + allLatLngs.get(allLatLngs.size() - 1).getLatLng().latitude) / 2), ((latLng.longitude + allLatLngs.get(allLatLngs.size() - 1).getLatLng().longitude) / 2));
                        options.position(latLng2).draggable(false).visible(true);
                        allLatLngs.add(new MyLatLng(latLng2, MyLatLng.UNABLE));
                        allLatLngsWithLine.add(latLng2);
                    } else {
                        options.position(latLng).draggable(false).visible(true);
                        allLatLngs.add(new MyLatLng(latLng, MyLatLng.ABLE));
                        allLatLngsWithLine.add(latLng);
                    }

                    Marker marker= amapView.getMapManager().getMapFunctions().addMarker(options);
                    marker.setAnchor(0.5f, 0.5f);
                    if (i== 1) {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark));
                    } else {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_multi_point_plot));
                    }
                    markers.add(marker);
                }
                drawLine(allLatLngsWithLine.get(allLatLngsWithLine.size() - 2), allLatLngsWithLine.get(allLatLngsWithLine.size() - 1));
            }
        }
    }

    /**
     * 计算周长
     */
    private void setPerimeter(){
        List<LatLng> points= polyline.getPoints();
        float f= 0;
        for (int i= 0; i< points.size() - 1; i++) {
            f= f+ AMapUtils.calculateLineDistance(points.get(i), points.get(i+ 1));
        }
        mPerimeterTv.setText("周长:" + f+"米");
    }

    /**
     * 计算面积
     */
    private void setArea(){
        List<LatLng> points2= new ArrayList<>();
        List<LatLng> points= polygon.getPoints();
        points2.addAll(points);
        BigDecimal bigDecimal= polygon_area(points2);
        mAreaTv.setText("面积:"+bigDecimal+"km²");
    }

    /**
     * 画线
     */
    private void drawLine(LatLng latLng, LatLng latLng2) {
        if (polyline == null) {
            polyline = amapView.getMapManager().getMapFunctions().addPolyline(new PolylineOptions().
                    add(latLng).width(10).color(Color.argb(255, 1, 1, 1)));
        } else {
            List<LatLng> points= polyline.getPoints();
            if (isEnd) {
                points.add(latLng);
                points.add(latLng2);
            } else {
                if (!points.contains(latLng)) {
                    points.add(latLng);
                }
                if (!points.contains(latLng2)) {
                    points.add(latLng2);
                }
            }
            polyline.setPoints(points);
            //计算周长
            setPerimeter();
        }
    }

    /**
     * 画多边形
     */
    private void drawRect() {
        if (polygon == null) {
            polygonOptions.addAll(allLatLngsWithLine);
            //创建多边形
            polygon = amapView.getMapManager().getMapFunctions().addPolygon(polygonOptions);
        } else {
            polygon.setPoints(allLatLngsWithLine);
        }
        //计算面积
        setArea();
    }

    /**
     * 获取面积
     * @paramring
     * @return
     */
    private BigDecimal polygon_area(List<LatLng> ring){
        double sJ= 6378137;
        double Hq= 0.017453292519943295;
        double c= sJ*Hq;
        double d= 0;
        if (3 > ring.size()) {
            return new BigDecimal( 0);
        }

        for (int i= 0; i< ring.size() - 1; i++){
            LatLng h= ring.get(i);
            LatLng k= ring.get(i+ 1);
            double u= h.longitude * c* Math.cos(h.latitude * Hq);
            double hhh= h.latitude * c;
            double v= k.longitude * c* Math.cos(k.latitude *Hq);
            d= d+ (u* k.latitude * c- v* hhh);
        }

        LatLng g1= ring.get(ring.size()-1);
        LatLng point= ring.get(0);
        double eee= g1.longitude * c* Math.cos(g1.latitude * Hq);
        double g2= g1.latitude * c;
        double k= point.longitude * c* Math.cos(point.latitude * Hq);
        d+= eee* point.latitude * c- k* g2;
        return new BigDecimal( 0.5*Math.abs(d)).divide(new BigDecimal(1000000));
    }

    /**
     * 绘制图形的颜色样式
     */
    private void createAreaStyle() {
        int fillColor= Color.parseColor("#11000000");
        // 设置多边形的边框颜色，32位 ARGB格式，默认为黑色
        polygonOptions.strokeWidth(10);
        polygonOptions.strokeWidth(10); // 设置多边形的填充颜色，32位ARGB格式
        polygonOptions.fillColor(fillColor); // 注意要加前两位的透明度 // 在地图上添加一个多边形（polygon）对象
    }

    /**
     * 获取所有点里离该点最近的点的索引值，阈值为1000，如果所有值都比2大，则表示没有最近的点(返回-1)
     *这个阈值可以继续做优化,根据地图缩放等级动态更改能获得更好体验
     * @paramlatLng
     */
    private int getNearestLatLng(LatLng latLng) {

        for (int i= 0; i< allLatLngs.size(); i++) {

            //判断两点间的直线距离

            float distance= AMapUtils.calculateLineDistance(latLng, allLatLngs.get(i).getLatLng());

            if (((int) distance) < 1000) {

                return i;

            }
        }
        return -1;
    }
}
