package com.example.zr.gxapplication.net.model;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.shandianshua.base.utils.SystemUtil;
import com.shandianshua.base.utils.SystemUtils;
import com.shandianshua.totoro.utils.emulatorintercept.JNIUtil;

public class PlatinumRequestBody extends BaseRequestBody {

  public int device_dpi;// true 普通参数 屏幕密度
  public String device_imsi;// true 普通参数 sim卡移动身份识别
  public String device_imei;// true 普通参数 sim卡移动身份识别
  public String device_mac;// 请求URL 手机mac地址
  public int api_level;// 请求URL 系统api level
  public String os_version;// 请求URL 系统版本号
  public String user_agent;// 请求URL user-agent
  public String android_id;// 请求URL Android手机设备系统ID
  public int screen_width;// 请求URL 设备屏幕宽
  public int screen_height;// 请求URL 设备屏幕高

  public PlatinumRequestBody() {
    super();
  }

  public PlatinumRequestBody init(Context context) {
    device_dpi = SystemUtil.getDpi(context);
    device_imsi = SystemUtil.getImsi(context);
    device_mac = JNIUtil.mac(context, Build.VERSION.SDK_INT);
    device_imei = SystemUtils.getImei(context);
    if (TextUtils.isEmpty(device_imei)) {
      device_imei = JNIUtil.getimei(context);
    }
    api_level = SystemUtil.getApiLevel();
    os_version = SystemUtil.getSdkReleaseVersion();
    user_agent = System.getProperty("http.agent");
    android_id = SystemUtil.getSecureAndroidID(context);
    screen_width = context.getResources().getDisplayMetrics().widthPixels;
    screen_height = context.getResources().getDisplayMetrics().heightPixels;
    return this;
  }
}
