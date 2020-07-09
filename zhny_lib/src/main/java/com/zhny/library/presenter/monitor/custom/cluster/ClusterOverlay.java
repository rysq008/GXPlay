package com.zhny.library.presenter.monitor.custom.cluster;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.zhny.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by liming
 */
public class ClusterOverlay implements AMap.OnCameraChangeListener, AMap.OnMarkerClickListener {

    private static final String TAG = "ClusterOverlay";
    private static final String EMPTY_NAME = "未知";

    private AMap mAMap;
    private Context mContext;
    private List<ClusterItem> clusterItems;  //坐标点数组
    private List<Cluster> mClusters;//聚合点数组
    private ClusterClickListener mClusterClickListener;
    private List<Marker> mAddMarkers = new ArrayList<>();
    private LruCache<String, BitmapDescriptor> mLruCache;
    private LruCache<String, BitmapDescriptor> mLruCacheName;
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    private Handler markerHandler; //更新marker
    private Handler signClusterHandler;  //计算marker
    private boolean mIsCanceled;
    private ClusterRender clusterRender;
    private BitmapDescriptor bitmapDes, zoomBitmapDescriptor;

    private float mapZoom;

    public void setClusterRender(ClusterRender clusterRender) {
        this.clusterRender = clusterRender;
    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     */
    public ClusterOverlay(AMap amap, Context context) {
        mapZoom = amap.getCameraPosition().zoom;
        //默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<String, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, String key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                if (oldValue.getBitmap() != null) oldValue.getBitmap().recycle();
            }
        };
        mLruCacheName = new LruCache<String, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, String key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                if (oldValue.getBitmap() != null) oldValue.getBitmap().recycle();
            }
        };

        mContext = context;
        mClusters = new ArrayList<>();
        this.mAMap = amap;
        amap.setOnCameraChangeListener(this);
        amap.setOnMarkerClickListener(this);
        initThreadHandler();//初始化线程
//        assignClusters();//把点进行聚合
    }

    public void updateMarkers(List<ClusterItem> params) {
        clusterItems = params;
        //把点进行聚合
        assignClusters();
    }

    //设置聚合点的点击事件
    public void setOnClusterClickListener(ClusterClickListener clusterClickListener) {
        this.mClusterClickListener = clusterClickListener;
    }

    //销毁资源
    public void onDestroy() {
        mIsCanceled = true;
        if (bitmapDes != null) bitmapDes.recycle();
        if (zoomBitmapDescriptor != null) zoomBitmapDescriptor.recycle();
        signClusterHandler.removeCallbacksAndMessages(null);
        markerHandler.removeCallbacksAndMessages(null);
        mSignClusterThread.quit();
        mMarkerHandlerThread.quit();
        for (Marker mAddMarker : mAddMarkers) {
            mAddMarker.remove();
        }
        mAddMarkers.clear();
        mLruCache.evictAll();
        mLruCacheName.evictAll();
    }

    //初始化Handler
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        markerHandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
        signClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }

    @Override
    public void onCameraChange(CameraPosition position) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        if (mapZoom != position.zoom) {
            mapZoom = position.zoom;
            //重新对点进行聚合
            assignClusters();

            mClusterClickListener.onCameraChangeFinish(mapZoom);
        }

    }

    //点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mClusterClickListener == null) return true;
        Cluster cluster = (Cluster) marker.getObject();
        if (cluster != null) {
            mClusterClickListener.onClusterClick(marker, cluster);
            return true;
        }
        return false;
    }


    /**
     * 将聚合元素添加至地图上
     */
    private void addClusterToMap(List<Cluster> clusters) {
        //已经添加过的聚合元素
        ArrayList<Marker> removeMarkers = new ArrayList<>(mAddMarkers);
        //做一个隐藏的动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }

        //然后再把所有的聚合元素重新添加
        for (Cluster cluster : clusters) {
            addSingleClusterToMap(cluster);
        }

    }

    private AlphaAnimation mADDAnimation = new AlphaAnimation(0, 1);

    /**
     * 将单个聚合元素添加至地图显示
     */
    private void addSingleClusterToMap(Cluster cluster) {
        MarkerOptions markerOptions = new MarkerOptions();
        bitmapDes = getBitmapDes(cluster);
        markerOptions.icon(bitmapDes).position(cluster.latLng);

        Marker marker = mAMap.addMarker(markerOptions);
        marker.setAnimation(mADDAnimation);
        marker.setObject(cluster);

        marker.startAnimation();
        cluster.marker = marker;
        mAddMarkers.add(marker);
    }


    /**
     * 这个貌似是处理多个坐标点
     */
    private void calculateClusters() {
        mIsCanceled = false;
        mClusters.clear();
        //判断现在地图上的区域是不是应该包含这个点，如果包含，就把点加到聚合数据里边，然后去通知 markerHandler 更新一下。
        if (clusterItems == null) return;
        try {
            for (ClusterItem clusterItem : clusterItems) {
                if (mIsCanceled) return;
                //判断坐标是否可以依附某个聚合点   不可以返回null
                Cluster cluster = getCluster(clusterItem, mClusters);
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem);
                } else {
                    //没有可以依附的聚合点的时候，就创建一个新的聚合点，加到聚合点数组里边
                    cluster = new Cluster(clusterItem.getPosition(),
                            clusterItem.getProvince(),
                            clusterItem.getCity(),
                            clusterItem.getDistrict(),
                            clusterItem.getName()
                    );
                    cluster.id = clusterItem.getId();
                    cluster.isOnline = clusterItem.isOnline();
                    cluster.isSelected = clusterItem.isSelected();
                    cluster.addClusterItem(clusterItem);
                    mClusters.add(cluster);
                }

            }

            //给后给控制 marker 的 handler 发送消息。把所有的聚合点信息发过去
            List<Cluster> clusters = new ArrayList<>(mClusters);
            Message message = Message.obtain();
            message.what = 0;
            message.obj = clusters;
            if (mIsCanceled) return;
            markerHandler.sendMessage(message);
        } catch (Exception e) {
            //ConcurrentModificationException
            e.printStackTrace();
        }
    }

    /**
     * 对点进行聚合
     */
    private void assignClusters() {
        mIsCanceled = true;
        signClusterHandler.removeMessages(0);//先把队列里边的消息移除
        signClusterHandler.sendEmptyMessage(0);//然后再发消息
    }


    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     */
    private Cluster getCluster(ClusterItem item, List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            if (mapZoom < ClusterLevelEnum.COUNTRY.level) { //全国
                return cluster;
            } else if (mapZoom < ClusterLevelEnum.PROVINCE.level) { //省份
                if (TextUtils.equals(item.getProvince(), cluster.province)) return cluster;
            } else if (mapZoom < ClusterLevelEnum.CITY.level) { // 市
                if (TextUtils.equals(item.getProvince(), cluster.province) &&
                        TextUtils.equals(item.getCity(), cluster.city)) return cluster;
            } else if (mapZoom < ClusterLevelEnum.DISTRICT.level) { //区
                if (TextUtils.equals(item.getProvince(), cluster.province) &&
                        TextUtils.equals(item.getCity(), cluster.city) &&
                        TextUtils.equals(item.getDistrict(), cluster.district)) return cluster;
            } else {
                return null;
            }
        }
        return null;
    }


    /**
     * 获取每个聚合点的绘制样式
     */
    private BitmapDescriptor getBitmapDes(Cluster cluster) {
        if (mapZoom < ClusterLevelEnum.DISTRICT.level) {
            if (mapZoom < ClusterLevelEnum.COUNTRY.level) { //全国
                zoomBitmapDescriptor = getRegionItemCount(4, cluster);
            } else if (mapZoom < ClusterLevelEnum.PROVINCE.level) { //省份
                zoomBitmapDescriptor = getRegionItemCount(6, cluster);
            } else if (mapZoom < ClusterLevelEnum.CITY.level) { // 市
                zoomBitmapDescriptor = getRegionItemCount(8, cluster);
            } else if (mapZoom < ClusterLevelEnum.DISTRICT.level) { //区
                zoomBitmapDescriptor = getRegionItemCount(10, cluster);
            } else {
                zoomBitmapDescriptor = getRegionItemName(cluster);
            }
        } else {
            zoomBitmapDescriptor = getRegionItemName(cluster);
        }
        return zoomBitmapDescriptor;
    }

    //获取地区名称
    private BitmapDescriptor getRegionItemCount(int level, Cluster cluster) {
        String key = level + "_" + cluster.name + "_" + cluster.province + "_" + cluster.city + "_" + cluster.city;
        BitmapDescriptor levelBitmapDescriptor = mLruCache.get(key);
        if (levelBitmapDescriptor == null) {
            String areaName = "";
            switch (level) {
                case 4:
                    areaName = "全国";
                    break;
                case 6:
                    areaName = TextUtils.isEmpty(cluster.province) ? EMPTY_NAME : cluster.province;
                    break;
                case 8:
                    areaName = TextUtils.isEmpty(cluster.city) ? EMPTY_NAME : cluster.city;
                    break;
                case 10:
                    areaName = TextUtils.isEmpty(cluster.district) ? EMPTY_NAME : cluster.district;
                    break;
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.lauout_cluster_bg, null);
            ((TextView) view.findViewById(R.id.tv_area_name)).setText(areaName);
            ((TextView) view.findViewById(R.id.tv_area_num)).setText(String.valueOf(cluster.getClusterCount()));
            levelBitmapDescriptor = BitmapDescriptorFactory.fromView(view);
            mLruCache.put(key, levelBitmapDescriptor);
        }
        return levelBitmapDescriptor;
    }

    //获取设备名称
    private BitmapDescriptor getRegionItemName(Cluster cluster) {
        String key = cluster.id + "_" + cluster.name + "_" + cluster.isSelected + "_" + cluster.isOnline;
        BitmapDescriptor nameBitmapDescriptor = mLruCacheName.get(key);
        if (nameBitmapDescriptor == null) {
            nameBitmapDescriptor = clusterRender.getDrawAble(cluster);
            mLruCacheName.put(key, nameBitmapDescriptor);
        }
        return nameBitmapDescriptor;
    }


//-----------------------辅助内部类用---------------------------------------------

    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * 处理market添加，更新等操作
     */
    class MarkerHandler extends Handler {

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 0) {//接收到在当前区域内应该显示的所有的聚合点，把聚合点加到地图上
                List<Cluster> clusters = (List<Cluster>) message.obj;
                addClusterToMap(clusters);
            }
        }
    }

    /**
     * 处理聚合点算法线程
     */
    class SignClusterHandler extends Handler {

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 0) {
                calculateClusters();
            }
        }
    }

}
