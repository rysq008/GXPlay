package com.example.zr.gxapplication.net.interceptor;

import android.content.Context;

import com.example.zr.gxapplication.utils.NetUtils;
import com.google.gson.internal.LinkedHashTreeMap;
import com.shandianshua.base.config.GlobalConfig;
import com.shandianshua.base.utils.AppUtils;
import com.shandianshua.base.utils.CollectionUtils;
import com.shandianshua.base.utils.NetUtils;
import com.shandianshua.base.utils.SystemUtils;
import com.shandianshua.base.utils.UdidUtils;
import com.shandianshua.totoro.utils.SharedPreUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: zhoulei date: 15/11/23.
 */
public class CommonHeaderInterceptor implements Interceptor {
  private final Map<String, String> globalStaticHeaders = new LinkedHashTreeMap<>();
  private final DynamicHeadersUpdater globalDynamicHeaderUpdater =
      new DynamicHeadersUpdater() {
        @Override
        public Map<String, String> getDynamicHeaders() {
          Map<String, String> headers = new HashMap<>();
          headers.put("Raikou-Network", NetUtils.getNetwork());
          return headers;
        }
      };

  private final Map<String, String> staticHeaders = new LinkedHashTreeMap<>();
  private final Set<DynamicHeadersUpdater> dynamicHeadersUpdaters = new HashSet<>();

  public CommonHeaderInterceptor() {
    this(null);
  }

  public CommonHeaderInterceptor(Map<String, String> staticHeaders) {
    putGlobalStaticHeadersInto(this.staticHeaders);
    putGlobalHeadersUpdaterInto(this.dynamicHeadersUpdaters);

    if (staticHeaders != null && staticHeaders.size() > 0) {
      this.staticHeaders.putAll(staticHeaders);
    }
  }

  public void addDynamicHeadersUpdater(DynamicHeadersUpdater dynamicHeadersUpdater) {
    if (dynamicHeadersUpdater != null) {
      synchronized (dynamicHeadersUpdaters) {
        dynamicHeadersUpdaters.add(dynamicHeadersUpdater);
      }
    }
  }

  public void removeDynamicHeadersUpdater(DynamicHeadersUpdater dynamicHeadersUpdater) {
    if (dynamicHeadersUpdater != null) {
      synchronized (dynamicHeadersUpdaters) {
        dynamicHeadersUpdaters.remove(dynamicHeadersUpdater);
      }
    }
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request origin = chain.request();
    return chain.proceed(addHeaders(origin));
  }

  private Request addHeaders(Request origin) {
    Request.Builder newRequestBuilder = origin.newBuilder();
    Map<String, String> headers = generateHeaders();
    if (headers != null && headers.size() > 0) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        if (entry == null) {
          continue;
        }
        newRequestBuilder.addHeader(entry.getKey(), entry.getValue());
      }
    }
    return newRequestBuilder.build();
  }

  private Map<String, String> generateHeaders() {
    Map<String, String> headers = new LinkedHashTreeMap<>();

    headers.putAll(staticHeaders);

    if (!CollectionUtils.isEmpty(dynamicHeadersUpdaters)) {
      for (Iterator<DynamicHeadersUpdater> iterator = dynamicHeadersUpdaters.iterator(); iterator
          .hasNext();) {
        DynamicHeadersUpdater updater = iterator.next();
        if (updater == null) {
          iterator.remove();
          continue;
        }
        Map<String, String> dynamicHeaders = updater.getDynamicHeaders();
        if (dynamicHeaders != null && dynamicHeaders.size() > 0) {
          headers.putAll(dynamicHeaders);
        }
      }
    }
    return headers;
  }

  private void putGlobalStaticHeadersInto(Map<String, String> headers) {
    if (globalStaticHeaders.isEmpty()) {
      initGlobalStaticHeaders();
    }
    headers.putAll(globalStaticHeaders);
  }

  private void putGlobalHeadersUpdaterInto(Set<DynamicHeadersUpdater> updaters) {
    updaters.add(globalDynamicHeaderUpdater);
  }

  private void initGlobalStaticHeaders() {
    Context context = GlobalConfig.getAppContext();
    globalStaticHeaders.put("Raikou-Event", "1001");
    globalStaticHeaders.put("Raikou-Udid", UdidUtils.getUdid(context));
    globalStaticHeaders.put("Raikou-Udid-Version", UdidUtils.getUdidVersion());
    globalStaticHeaders.put("Raikou-Package-Name", AppUtils.getPackageName(context));
    globalStaticHeaders
        .put("Raikou-Version-Code", String.valueOf(AppUtils.getVersionCode(context)));
    globalStaticHeaders.put("Raikou-Version-Name", AppUtils.getVersionName(context));
    globalStaticHeaders.put("Raikou-Device-Model", URLEncoder.encode(SystemUtils.getDeviceModel()));
    globalStaticHeaders.put("Raikou-Device-Brand", URLEncoder.encode(SystemUtils.getDeviceBrand()));
    globalStaticHeaders.put("Raikou-Device-Product",
        URLEncoder.encode(SystemUtils.getDeviceProduct()));
    globalStaticHeaders.put("Raikou-Device-Manufacturer",
        URLEncoder.encode(SystemUtils.getDeviceManufacturer()));
    globalStaticHeaders.put("Raikou-Device-Radio-Version",
        URLEncoder.encode(SystemUtils.getDeviceRadioVersion()));
    globalStaticHeaders.put("Raikou-Device-Sdk-Version",
        String.valueOf(SystemUtils.getDeviceVersion()));
    globalStaticHeaders.put("Raikou-Device-Sdk-Version-Name",
        String.valueOf(SystemUtils.getDeviceVersionName()));
    globalStaticHeaders.put("Raikou-Has-Nfc-Module",
        String.valueOf(SystemUtils.hasNfcModel(context)));
    globalStaticHeaders.put("Raikou-Unionid",
        null == SharedPreUtil.getUnionId() ? "" : SharedPreUtil.getUnionId());
    globalStaticHeaders.put("Login-Type", "2");
    globalStaticHeaders.put("Raikou-Device-Imsi", SystemUtils.getImsi(context));
  }

  public interface DynamicHeadersUpdater {
    Map<String, String> getDynamicHeaders();
  }
}
