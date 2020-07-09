package com.zhny.library.presenter.fence.view;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlay;
import com.zhny.library.R;
import com.zhny.library.databinding.ActivityEditFenceBinding;
import com.zhny.library.presenter.fence.FenceConstant;
import com.zhny.library.presenter.fence.base.FenceBaseActivity;
import com.zhny.library.presenter.fence.helper.FenceHelper;
import com.zhny.library.presenter.fence.listener.OnFenceDrawViewListener;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.vo.DrawFence;
import com.zhny.library.presenter.fence.util.FenceMapUtil;
import com.zhny.library.presenter.fence.util.PositionUtil;
import com.zhny.library.presenter.fence.viewmodel.FenceEditModel;
import com.zhny.library.utils.DataUtil;
import com.zhny.library.utils.DisplayUtils;
import com.zhny.library.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class EditFenceActivity extends FenceBaseActivity implements AMap.OnMapClickListener,
      OnFenceDrawViewListener, AMap.OnMapTouchListener {

    private ActivityEditFenceBinding binding;
    private FenceEditModel viewModel;
    private MapView mapView;
    private AMap aMap;
    private TileOverlay tileOverlay;
    private MarkerOptions markerOptions, handleOptions;
    private PolylineOptions polylineOptions;
    private PolygonOptions polygonOptions;
    private List<DrawFence> markerData = new ArrayList<>();
    private Polygon polygon;
    private Polyline polyline;
    private BitmapDescriptor bitmapDescriptor, handleBitmapDescriptor;
    private boolean isDrawPoint;
    private List<LatLng> latLngs = new ArrayList<>();
    private int fenceId = -1;
    private String fenceName;

    private FenceMapUtil fenceMapUtil = new FenceMapUtil();
    private boolean isCross;

    private UiSettings uiSettings;

    @Override
    public void initParams(Bundle params) {
        if (params != null) {
            fenceId = params.getInt(FenceConstant.FENCE_ID);
            fenceName = params.getString(FenceConstant.FENCE_NAME);
            params.clear();
        }
    }

    @Override
    protected Object onBindView(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(FenceEditModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_fence);
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapClickListener(this);
        aMap.setOnMapTouchListener(this);
        aMap.setOnMarkerClickListener(marker -> true);

        return binding.getRoot();
    }

    @Override
    public void initBusiness() {
        viewModel.setParams(this);
        binding.setLifecycleOwner(this);
        setToolBarTitle(TextUtils.isEmpty(fenceName) ? getString(R.string.fence_edit_title) : fenceName);

        uiSettings = aMap.getUiSettings();

        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        tileOverlay = MapUtils.addRemoteOverlay(aMap);

        View view = LayoutInflater.from(this).inflate(R.layout.fence_maker_view_image, null);
        ((ImageView) view.findViewById(R.id.iv_draw_fence_marker)).setImageResource(R.drawable.icon_fence_add_maker);
        bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        markerOptions = new MarkerOptions()
                .setFlat(true)
                .icon(bitmapDescriptor).anchor(0.5f, 0.5f);

        View handleView = LayoutInflater.from(this).inflate(R.layout.fence_handle_maker_view_image, null);
        ((ImageView) handleView.findViewById(R.id.iv_draw_fence_handle_marker)).setImageResource(R.drawable.handle);
        handleBitmapDescriptor = BitmapDescriptorFactory.fromView(handleView);
        handleOptions = new MarkerOptions()
                .icon(handleBitmapDescriptor)
                .setFlat(true).zIndex(999);

        polylineOptions = new PolylineOptions()
                .width(DisplayUtils.dp2px(2))
                .color(Color.parseColor("#009688"));

        polygonOptions = new PolygonOptions()
                .strokeWidth(DisplayUtils.dp2px(2))
                .strokeColor(Color.parseColor("#009688"))
                .fillColor(Color.parseColor("#30009688"));

        binding.menuChart.setPieData(getPieDate());
        binding.menuChart.setStartAngle(180);
        binding.menuChart.setPieShowAngle(180);
        binding.menuChart.setOnSelectMenuListener(this);
        binding.menuChart.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        isDrawPoint = false;
        if (fenceId != -1) {
            getFenceDetails(fenceId);
        }
    }

    //查询围栏详情数据
    private void getFenceDetails(int fenceId) {
        viewModel.getFenceDetails(fenceId).observe(this, baseDto -> {
            if (baseDto.getContent() == null) {
                Toast.makeText(this, baseDto.getMsg(), Toast.LENGTH_SHORT).show();
                return;
            }
            clear();
            Fence fence = baseDto.getContent();
            latLngs.clear();
            latLngs.addAll(FenceHelper.getFenceLatLngs(fence.pointList));
            onReset();
        });
    }

    //移动到聚合点
    private void movePoints(List<LatLng> latLngs) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            boundsBuilder.include(latLng);
        }
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), DisplayUtils.dp2px(60)));
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
        isDrawPoint = false;
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
        clear();
        if (bitmapDescriptor != null) bitmapDescriptor.recycle();
        if (handleBitmapDescriptor != null) handleBitmapDescriptor.recycle();
        if (mapView != null) mapView.onDestroy();
        if (binding != null) binding.unbind();
    }

    private void clear() {
        for (DrawFence fence : markerData) {
            if (fence.marker != null) {
                if (!fence.marker.isRemoved()) fence.marker.remove();
            }
        }
        markerData.clear();
        if (polygon != null){
            polygon.remove();
            polygon = null;
        }
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (isDrawPoint) {
            drawPoint(latLng);
        }
    }


    //绘制marker点
    private void drawPoint(LatLng latLng) {
        Marker marker = aMap.addMarker(markerOptions);
        marker.setPosition(latLng);
        marker.setZIndex(999);
        markerData.add(new DrawFence(marker.getId(), marker));
        drawLine();
    }

    //将marker点连接线
    private void drawLine() {
        binding.tvFenceArea.setText(String.format("%s 0.0亩", getText(R.string.fence_area)));
        binding.setCanNext(false);

        if (markerData.size() < 2) return;
        //绘制线
        if (markerData.size() < 3) {
            if (polyline == null) {
                polyline = aMap.addPolyline(polylineOptions);
            }
            polyline.setVisible(true);
            polyline.setZIndex(999);
            polyline.setPoints(getPointsFromMarkers(markerData));
            return;
        }
        //绘制面
        if (polyline != null) {
            polyline.setVisible(false);
            polyline = null;
        }
        if (polygon == null) polygon = aMap.addPolygon(polygonOptions);
        polygon.setZIndex(999);
        List<LatLng> points = getPointsFromMarkers(markerData);
        polygon.setPoints(points);
        isCross = checkCross(points);
        polygon.setStrokeColor(Color.parseColor(isCross ? "#ff6666" : "#009688"));
        polygon.setFillColor(Color.parseColor(isCross ? "#30ff6666" : "#30009688"));

        float area = AMapUtils.calculateArea(points) * 0.0015f; //平方米转为亩
        binding.tvFenceArea.setText(String.format("%s %s亩", getText(R.string.fence_area), DataUtil.get1Point(area)));

        binding.setCanNext(!isCross);
    }

    @Override
    public void onDrawPoint() {
        isDrawPoint = true;
    }

    @Override
    public void onBackPoint() {
        removeHandleMarker();
        if (markerData.size() == 0)  return;
        if (markerData.size() - 1 < 3) {
            Toast.makeText(this, getString(R.string.fence_edit_point_least_three), Toast.LENGTH_LONG).show();
            return;
        }
        DrawFence fence = markerData.get(markerData.size() - 1);
        if (fence.marker != null && !fence.marker.isRemoved()) {
            fence.marker.remove();
        }
        markerData.remove(fence);
        drawLine();
    }

    @Override
    public void onClearAll() { //清空
        binding.tvFenceArea.setText(String.format("%s 0.0亩", getText(R.string.fence_area)));
        binding.setCanNext(false);
        isDrawPoint = false;
        clear();
        removeHandleMarker();
    }

    @Override
    public void onReset() {
        onClearAll();
        if (latLngs.size() > 0) {
            movePoints(latLngs);
            for (LatLng latLng : latLngs) {
                drawPoint(latLng);
            }
        }
    }

   public void editFenceNext(View v) {
       if (markerData.size() < 3) {
           Toast.makeText(this, getString(R.string.fence_edit_point_least_three), Toast.LENGTH_LONG).show();
           return;
       }
       if (isCross) {
           Toast.makeText(this, getString(R.string.fence_edit_point_line_cross), Toast.LENGTH_LONG).show();
           return;
       }
       Bundle bundle = new Bundle();
       bundle.putBoolean(FenceConstant.IS_ADD, false);
       bundle.putInt(FenceConstant.FENCE_ID, fenceId);
       bundle.putString(FenceConstant.FENCE_DRAW_POINTS, getMarkersPoint());
       startActivity(FenceInfoActivity.class, bundle);

       finish();
   }

    private String getMarkersPoint() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < markerData.size(); i++) {
            LatLng position = markerData.get(i).marker.getPosition();
            buffer.append(position.longitude).append(",");
            buffer.append(position.latitude);
            buffer.append(i == markerData.size() - 1 ? "" : ";");
        }
        return buffer.toString();
    }


    //放大地图
    public void onMapZoomIn(View view) {
        super.onMapZoomIn(aMap);
    }

    //缩小地图
    public void onMapZoomOut(View view) {
        super.onMapZoomOut(aMap);
    }

    public boolean checkCross(List<LatLng> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                LatLng latLng;
                if (j == points.size() - 1) {
                    latLng = points.get(0);
                } else {
                    latLng = points.get(j + 1);
                }

                PointF point = aMap.getProjection().toMapLocation(points.get(i));
                PointF point1 = aMap.getProjection().toMapLocation(points.get(i + 1));
                PointF point2 = aMap.getProjection().toMapLocation(points.get(j));
                PointF point3 = aMap.getProjection().toMapLocation(latLng);

                if (fenceMapUtil.ab_cross_cd(point, point1, point2, point3) == 1) {
                    return true;
                }
            }
        }
        return false;
    }



    private static final float DISTANCE_HANDLE_CHECK = 120f; //可适当调整
    private int checkPos;
    private Marker touchMarker, handleMarker;
    private int oldHandleMarkerX = 0;
    private int oldHandleMarkerY = 0;
    private int oldMarkerX = 0;
    private int oldMarkerY = 0;
    private boolean isTouchHandleMarker;

    @Override
    public void onTouch(MotionEvent event) {
        if (markerData.size() < 3) return;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point downPoint = new Point((int)event.getX(), (int)event.getY());
                checkPos = getNearestIndex(downPoint);
                if (checkPos > -1) {
                    uiSettings.setScrollGesturesEnabled(false);
                    touchMarker = markerData.get(checkPos).marker;
                    showHandleMarker(touchMarker);
                    isTouchHandleMarker = checkTouchHandleMarker(downPoint);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Point movePoint = new Point((int)event.getX(), (int)event.getY());
                if (checkPos > -1 && handleMarker != null && isTouchHandleMarker) {
                    remoteControl(movePoint);
                }
                break;
            case MotionEvent.ACTION_UP:
                uiSettings.setScrollGesturesEnabled(true);
                updateHandleAngle();
                checkPos = -1;
                break;
        }
    }


    //改变值
    public boolean checkTouchHandleMarker(Point point) {
        if (handleMarker != null) {
            Point markerPoint = aMap.getProjection().toScreenLocation(handleMarker.getPosition());
            if (Math.abs(point.x - markerPoint.x) <= DISTANCE_HANDLE_CHECK && Math.abs(point.y - markerPoint.y) <= DISTANCE_HANDLE_CHECK) {
                uiSettings.setScrollGesturesEnabled(false);
                oldHandleMarkerX = point.x;
                oldHandleMarkerY = point.y;
                oldMarkerX = markerPoint.x;
                oldMarkerY = markerPoint.y;
                isTouchHandleMarker = true;
                return true;
            }
        }
        return false;
    }

    //控制marker并重绘
    private void remoteControl(Point point2) {
        Point point = new Point();
        int offSetX = point2.x - oldHandleMarkerX;
        int offSetY = point2.y - oldHandleMarkerY;

        point.set(oldMarkerX + offSetX, oldMarkerY + offSetY);
        LatLng latLng = aMap.getProjection().fromScreenLocation(point);
        if (touchMarker != null) {
            handleMarker.setPosition(latLng);
            touchMarker.setPosition(latLng);
            if (markerData.size() < 2) return;
            drawLine();
        }
    }

    //获取所有点中离该点最近的点的索引值
    private int getNearestIndex(Point downPoint) {
        for (int i = 0; i < markerData.size(); i++) {
            Point point = aMap.getProjection().toScreenLocation(markerData.get(i).marker.getPosition());
            //此处可优化 -> 取最小路径
            if (Math.abs(downPoint.x - point.x) < DISTANCE_HANDLE_CHECK && Math.abs(downPoint.y - point.y) < DISTANCE_HANDLE_CHECK) {
                return i;
            }
        }
        return -1;
    }



    //显示拖把
    private void showHandleMarker(Marker touchMarker) {
        removeHandleMarker();
        if (touchMarker != null) {
            handleMarker = aMap.addMarker(handleOptions);
            handleMarker.setPosition(touchMarker.getPosition());
        }
    }


    private void updateHandleAngle() {
        if (touchMarker != null && handleMarker != null) {
            float angle = getHandleAngle(touchMarker.getPosition());
            handleMarker.setRotateAngle(angle);
        }
    }


    //获取拖把角度
    private float getHandleAngle(LatLng touchLatlng) {
        int size = markerData.size();
        if (size < 3) return 0f;
        double angle = 0f;
        Point left = null, right = null;
        Point middle = aMap.getProjection().toScreenLocation(touchLatlng);
        int middleIndex = getNearestIndex(middle);
        if (middleIndex > -1) {
            if (middleIndex == 0) {
                left = aMap.getProjection().toScreenLocation(markerData.get(size - 1).marker.getPosition());
                right = aMap.getProjection().toScreenLocation(markerData.get(1).marker.getPosition());
            } else if (middleIndex == markerData.size() - 1) {
                left = aMap.getProjection().toScreenLocation(markerData.get(middleIndex - 1).marker.getPosition());
                right = aMap.getProjection().toScreenLocation(markerData.get(0).marker.getPosition());
            } else {
                left = aMap.getProjection().toScreenLocation(markerData.get(middleIndex - 1).marker.getPosition());
                right = aMap.getProjection().toScreenLocation(markerData.get(middleIndex + 1).marker.getPosition());
            }
        }
        if (left != null && right != null) {
            angle = PositionUtil.getAngle(middle, left, right);
        }
        return (float) angle;
    }

    //清除拖把
    private void removeHandleMarker() {
        if (handleMarker != null) {
            handleMarker.remove();
            handleMarker = null;
        }
    }


}