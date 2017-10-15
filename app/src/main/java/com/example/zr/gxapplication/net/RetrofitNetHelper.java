package com.example.zr.gxapplication.net;

import com.example.zr.gxapplication.BuildConfig;
import com.example.zr.gxapplication.net.api.UserApi;
import com.example.zr.gxapplication.net.interceptor.CommonHeaderInterceptor;
import com.example.zr.gxapplication.net.interceptor.LoggingRequestInterceptor;
import com.example.zr.gxapplication.net.interceptor.LoggingResponseInterceptor;
import com.example.zr.gxapplication.net.interceptor.ReadableErrorInterceptor;
import com.example.zr.gxapplication.net.interceptor.RetryRequestInterceptor;
import com.example.zr.gxapplication.utils.LogUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private static Retrofit customRetrofit;

    private static UserApi userApi;


    private static RetryRequestInterceptor eventRetryRequestInterceptor;

    public static boolean isDebug() {
        return BASE_URL.equals(DEBUG_BASE_URL);
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


    private static OkHttpClient getOkHttpClient() {
//    String signKey = SdsJniKeys.getTotoroSecurityKey();
//    RSAPublicKey publicKey = RSAEncrypt.createPublicKey(SdsJniKeys.getRSAPublicKey());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TOTORO_CONNECT_TIMEOUT_INTERVAL, TimeUnit.SECONDS)
                .readTimeout(TOTORO_READ_TIMEOUT_INTERVAL, TimeUnit.SECONDS)
                .writeTimeout(TOTORO_WRITE_TIMEOUT_INTERVAL, TimeUnit.SECONDS).build();
        List<Interceptor> interceptors = okHttpClient.interceptors();
        interceptors.add(new CommonHeaderInterceptor());
//    interceptors.add(new SignRequestV2Interceptor(signKey));
        interceptors.add(LoggingRequestInterceptor.getInstance());
        interceptors.add(readableErrorInterceptor);
//    interceptors.add(new SignCheckInterceptor(signKey));
        interceptors.add(LoggingResponseInterceptor.getInstance());
//    interceptors.add(new RSAResponseInterceptor(publicKey));
        return okHttpClient;
    }

    public static synchronized UserApi getUserApi() {
        if (userApi == null) {
            userApi = getCommonRetrofit().create(UserApi.class);
        }
        return userApi;
    }

    private synchronized static Retrofit getCommonRetrofit() {
        if (commonRetrofit == null) {
            commonRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return commonRetrofit;
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
