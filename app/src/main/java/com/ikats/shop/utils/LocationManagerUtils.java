//package com.ikats.shop.utils;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.location.LocationManager;
//import android.text.TextUtils;
//
//import androidx.fragment.app.FragmentActivity;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.zhny.library.databinding.FragmentTableDataBindingImpl;
//
//public class LocationManagerUtils {
//
//    private static AMapLocationClient aMapLocationClient;
//    private static AMapLocationListener aMapLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation amapLocation) {
//            if (amapLocation != null && amapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
//                {
////                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    if (TextUtils.isEmpty(amapLocation.getAddress())) {
//                        return;
//                    }
//                    ShareUtils.saveString("lat", String.valueOf(amapLocation.getLatitude()));
//                    ShareUtils.saveString("lon", String.valueOf(amapLocation.getLongitude()));
//                    ShareUtils.saveString("adr", (amapLocation.getAddress()));
//                    if (callback != null) {
//                        if (null != progressDialog) {
//                            progressDialog.dismiss();
//                        }
//                        callback.onLocatonResultHandle(amapLocation);
//                        callback = null;
//                    }
//                    aMapLocationClient.stopLocation();
//                }
//            } else {
//                if (null != progressDialog) {
//                    progressDialog.dismiss();
//                }
//            }
//        }
//    };
//    private static ProgressDialog progressDialog;
//
//    private static void initLocationInfo(Activity context) {
//        if (aMapLocationClient == null) {
//            aMapLocationClient = new AMapLocationClient(context.getApplicationContext());
//            AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
//            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            aMapLocationClient.setLocationOption(aMapLocationClientOption);
//            aMapLocationClient.setLocationListener(aMapLocationListener);
//        }
//    }
//
//    public static void requesetLocation(FragmentActivity ct, LocationResultListener callback) {
//        initLocationInfo(ct);
//        rxPermissions = new RxPermissions(ct);
//        LocationManagerUtils.callback = callback;
//        if (rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            if (InternetUtil.isConnected(ct)) {
//                if (isGpsOPen(ct)) {
//                    if (!ct.isFinishing())
//                        progressDialog = ProgressDialog.show(ct, "", "定位中。。。");
//                    if (callback != null) {
//                        LocationManagerUtils.callback.onLocationPreprocess(ct);
//                    }
//                    aMapLocationClient.startLocation();
//                } else {
//                    openGPS(ct);
//                }
//            } else {
//                openWifi(ct);
//            }
//        } else {
//            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe();
//        }
//    }
//
//    static LocationResultListener callback;
//    static RxPermissions rxPermissions;
//
//    /**
//     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
//     *
//     * @param context
//     * @return true 表示开启
//     */
//    public static final boolean isGpsOPen(final Activity context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
//        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (gps || network) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 强制帮用户打开GPS
//     *
//     * @param context
//     */
//    public static final void openGPS(Activity context) {
//        new AlertDialog.Builder(context).setTitle("提示").setMessage("请打开gps定位！").
//                setPositiveButton("确定", (DialogInterface dialog, int which) -> {
//                    Intent intent = new Intent();
//                    intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
//                    context.startActivityForResult(intent, 1315);
//                })
//                .setNegativeButton("取消", (DialogInterface dialog, int which) -> {
//                    context.finish();
//                }).setCancelable(false).show();
//
////        Intent GPSIntent = new Intent();
////        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
////        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
////        GPSIntent.setData(Uri.parse("custom:3"));
////        try {
////            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
////        } catch (PendingIntent.CanceledException e) {
////            e.printStackTrace();
////        }
//    }
//
//    public static final void openWifi(Activity context) {
//        new AlertDialog.Builder(context).setTitle("提示").setMessage("请检测网络连接是否正常").
//                setPositiveButton("确定", (DialogInterface dialog, int which) -> {
//                    InternetUtil.openWirelessSettings(context);
//                })
//                .setNegativeButton("取消", (DialogInterface dialog, int which) -> {
//                    context.finish();
//                }).setCancelable(false).show();
//    }
//
//    public interface LocationResultListener {
//        public default void onLocationPreprocess(Activity ct) {
//        }
//
//        ;
//
//        void onLocatonResultHandle(AMapLocation amapLocation);
//    }
//}
