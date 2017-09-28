package com.example.zr.gxapplication.net;

import android.content.Context;
import android.support.annotation.Nullable;

import com.shandianshua.base.config.GlobalConfig;
import com.shandianshua.base.factory.GsonFactory;
import com.shandianshua.base.utils.RSAEncrypt;
import com.shandianshua.jni.sds.SdsJniKeys;
import com.shandianshua.storage.Storage;
import com.shandianshua.storage.StorageFactory;
import com.shandianshua.totoro.BuildConfig;
import com.shandianshua.totoro.data.net.api.CustomApi;
import com.shandianshua.totoro.data.net.api.EventApi;
import com.shandianshua.totoro.data.net.api.LotteryApi;
import com.shandianshua.totoro.data.net.api.UserApi;
import com.shandianshua.totoro.data.net.interceptor.CommonHeaderInterceptor;
import com.shandianshua.totoro.data.net.interceptor.LoggingRequestInterceptor;
import com.shandianshua.totoro.data.net.interceptor.LoggingResponseInterceptor;
import com.shandianshua.totoro.data.net.interceptor.RSARequestInterceptor;
import com.shandianshua.totoro.data.net.interceptor.RSAResponseInterceptor;
import com.shandianshua.totoro.data.net.interceptor.ReadableErrorInterceptor;
import com.shandianshua.totoro.data.net.interceptor.RetryRequestInterceptor;
import com.shandianshua.totoro.data.net.interceptor.SignCheckInterceptor;
import com.shandianshua.totoro.data.net.interceptor.SignRequestV2Interceptor;
import com.shandianshua.totoro.utils.net.LogUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * author: zhoulei date: 15/11/23.
 */
public class RetrofitNetHelper {

  private RetrofitNetHelper() {

  }

  private static final String RELEASE_BASE_URL = "";
  private static final String DEBUG_BASE_URL = "";
  private static String BASE_URL = BuildConfig.URL_HOST;
  private static String LOTTERY_URL = BuildConfig.LOTTERY_HOST;
  private static final String WX_BASE_TOKEN_URL = "https://api.weixin.qq.com";
  private static final int TOTORO_CONNECT_TIMEOUT_INTERVAL = 30;
  private static final int TOTORO_READ_TIMEOUT_INTERVAL = 30;
  private static final int TOTORO_WRITE_TIMEOUT_INTERVAL = 30;

  private static Retrofit commonRetrofit;

  private static Retrofit eventRetrofit;

  private static Retrofit lotteryRetrofit;

  private static Retrofit customRetrofit;

  private static UserApi userApi;

  private static EventApi eventApi;

  private static LotteryApi lotteryApi;

  private static RetryRequestInterceptor eventRetryRequestInterceptor;

  public static boolean isDebug() {
    return BASE_URL.equals(DEBUG_BASE_URL);
  }

  public static void sendEventRetryTrigger() {
    getEventRetryRequestInterceptor().triggerRetry();
  }

  private static final ReadableErrorInterceptor readableErrorInterceptor;
  static {
    readableErrorInterceptor = ReadableErrorInterceptor.getInstance();
    readableErrorInterceptor.setHttpErrorListener(new ReadableErrorInterceptor.HttpErrorListener() {
      @Override
      public void onHttpFailed(Request request, Response response) {
        LogUtils.sendHttpFailedLog(request, response);
      }

      @Override
      public void onHttpError(Request request, Throwable throwable) {
        LogUtils.sendHttpExceptionLog(request, throwable);
      }
    });
  }

  private static RetryRequestInterceptor getEventRetryRequestInterceptor() {
    if (eventRetryRequestInterceptor == null) {
      final Context appContext = GlobalConfig.getAppContext();
      eventRetryRequestInterceptor = new RetryRequestInterceptor(appContext,
          new RetryRequestInterceptor.RetryConfig() {
            @Override
            public long minRetryDuration() {
              return 1000L * 20;
            }

            @Override
            public long life() {
              return RetryRequestInterceptor.RetryConfig.LIFE_FOREVER;
            }

            @Override
            public int maxRetryTimes() {
              return 15;
            }

            @Override
            public Storage storage() {
              return StorageFactory.getDESFileStorage(appContext.getFilesDir().getAbsolutePath()
                  + File.separator + "wtf_fuck",
                  SdsJniKeys.getDESKeyNative(), 0);
            }

            @Override
            public OkHttpClient okHttpClient() {
              OkHttpClient okHttpClient = new OkHttpClient();
              List<Interceptor> interceptors = okHttpClient.interceptors();
              interceptors.add(LoggingRequestInterceptor.getInstance());
              interceptors.add(LoggingResponseInterceptor.getInstance());
              return okHttpClient;
            }

            @Override
            public boolean isRetryRequest(Request request) {
              // if upload task event failed, retry
              String url = request.urlString();
              return url.endsWith("/task-detail")
                  || url.endsWith("/launch-task-detail");
            }

            @Nullable
            @Override
            public RetryRequestInterceptor.RetryResultListener retryResultListener() {
              return new RetryRequestInterceptor.RetryResultListener() {
                @Override
                public void onRetrySuccess(Request request, Response response) {

                }

                @Override
                public void onRetryError(Request request, IOException exception) {
                  LogUtils.sendHttpExceptionLog(request, exception);
                }

                @Override
                public void onRetryFailed(Request request, Response response) {
                  LogUtils.sendHttpFailedLog(request, response);
                }

                @Override
                public void onAbortRetry(Request request, long deadLine, int retryTimes) {
                  LogUtils.sendRetryAbortLog(request, deadLine, retryTimes);
                }
              };
            }
          });
    }
    return eventRetryRequestInterceptor;
  }

  private static OkHttpClient getOkHttpClient() {
    String signKey = SdsJniKeys.getTotoroSecurityKey();
    RSAPublicKey publicKey = RSAEncrypt.createPublicKey(SdsJniKeys.getRSAPublicKey());

    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(TOTORO_CONNECT_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setReadTimeout(TOTORO_READ_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setWriteTimeout(TOTORO_WRITE_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    List<Interceptor> interceptors = okHttpClient.interceptors();
    interceptors.add(new CommonHeaderInterceptor());
    interceptors.add(new SignRequestV2Interceptor(signKey));
    interceptors.add(LoggingRequestInterceptor.getInstance());
    interceptors.add(readableErrorInterceptor);
    interceptors.add(new SignCheckInterceptor(signKey));
    interceptors.add(LoggingResponseInterceptor.getInstance());
    interceptors.add(new RSAResponseInterceptor(publicKey));
    return okHttpClient;
  }

  private static OkHttpClient getEventOkHttpClient() {
    final String signKey = SdsJniKeys.getTotoroSecurityKey();
    final RSAPublicKey publicKey = RSAEncrypt.createPublicKey(SdsJniKeys.getRSAPublicKey());

    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(TOTORO_CONNECT_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setReadTimeout(TOTORO_READ_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setWriteTimeout(TOTORO_WRITE_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    List<Interceptor> interceptors = okHttpClient.interceptors();
    interceptors.add(new CommonHeaderInterceptor());
    interceptors.add(new SignRequestV2Interceptor(signKey));
    interceptors.add(LoggingRequestInterceptor.getInstance());
    interceptors.add(new RSARequestInterceptor(publicKey));
    interceptors.add(readableErrorInterceptor);
    interceptors.add(new SignCheckInterceptor(signKey));
    interceptors.add(LoggingResponseInterceptor.getInstance());
    interceptors.add(new RSAResponseInterceptor(publicKey));
    interceptors.add(getEventRetryRequestInterceptor());

    return okHttpClient;
  }

  private static OkHttpClient getLotteryHttpClient() {
    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(TOTORO_CONNECT_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setReadTimeout(TOTORO_READ_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    okHttpClient.setWriteTimeout(TOTORO_WRITE_TIMEOUT_INTERVAL, TimeUnit.SECONDS);
    List<Interceptor> interceptors = okHttpClient.interceptors();
    interceptors.add(new CommonHeaderInterceptor());
    interceptors.add(LoggingRequestInterceptor.getInstance());
    interceptors.add(readableErrorInterceptor);
    interceptors.add(LoggingResponseInterceptor.getInstance());
    return okHttpClient;
  }

  public static synchronized UserApi getUserApi() {
    if (userApi == null) {
      userApi = getCommonRetrofit().create(UserApi.class);
    }
    return userApi;
  }

  public static synchronized EventApi getEventApi() {
    if (eventApi == null) {
      eventApi = getEventRetrofit().create(EventApi.class);
    }
    return eventApi;
  }

  public static synchronized LotteryApi getLotteryApi() {
    if (lotteryApi == null) {
      lotteryApi = getLotteryRetrofit().create(LotteryApi.class);
    }
    return lotteryApi;
  }

  public static synchronized CustomApi getCustomApi(String baseUrl) {
    return getCustomRetrofit(baseUrl).create(CustomApi.class);
  }

  private synchronized static Retrofit getCommonRetrofit() {
    if (commonRetrofit == null) {
      commonRetrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(GsonFactory.getGson()))
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .client(getOkHttpClient())
          .baseUrl(BASE_URL)
          .build();
    }
    return commonRetrofit;
  }

  private synchronized static Retrofit getEventRetrofit() {
    if (eventRetrofit == null) {
      eventRetrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(GsonFactory.getGson()))
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .client(getEventOkHttpClient())
          .baseUrl(BASE_URL)
          .build();
    }
    return eventRetrofit;
  }

  private synchronized static Retrofit getLotteryRetrofit() {
    if (lotteryRetrofit == null) {
      lotteryRetrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(GsonFactory.getGson()))
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .client(getLotteryHttpClient())
          .baseUrl(LOTTERY_URL)
          .build();
    }
    return lotteryRetrofit;
  }

  private synchronized static Retrofit getCustomRetrofit(String baseUrl) {
    customRetrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonFactory.getGson()))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(getLotteryHttpClient())
        .baseUrl(baseUrl)
        .build();
    return customRetrofit;
  }

  public static void resetData() {
    commonRetrofit = null;
    userApi = null;
  }

  public static void setBaseURL(boolean isAppDebug) {
    if (isAppDebug) {
      BASE_URL = "http://pikachu.totoro.test.shandianshua.com";
      LOTTERY_URL = "https://1.test.longmaosoft.com";
    } else {
      BASE_URL = "http://pikachu.totoro.shandianshua.com";
      LOTTERY_URL = "https://1.longmaosoft.com";
    }
  }
}
