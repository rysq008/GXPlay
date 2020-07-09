package com.zhny.library.https.retrofit;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhny.library.common.Constant;
import com.zhny.library.https.interceptor.TokenInterceptor;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 普通的网络请求用到的Retrofit
 */

public class RequestRetrofit {
    private static final String TAG = "RequestRetrofit";

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public static <T> T getInstance(Context context, LoadingDialog dialog, final Class<T> service) {
        if (okHttpClient == null) {
            synchronized (RequestRetrofit.class) {
                if(okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(Constant.Server.TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(Constant.Server.TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(Constant.Server.TIME_OUT, TimeUnit.SECONDS)
                            .addNetworkInterceptor(new HttpLoggingInterceptor(message -> {
                                Log.d(TAG, "==> : " + message);
                                if (message.contains("timeout")) {
//                                    Toast.makeText(context, "请求超时!", Toast.LENGTH_SHORT).show();
                                }
                            }).setLevel(HttpLoggingInterceptor.Level.BODY))
                            .addNetworkInterceptor(new StethoInterceptor())
//                            .authenticator(new TokenAuthenticator(context))
                            .addInterceptor(new TokenInterceptor(context, dialog))
                            .build();
                }
            }
        }

        if (retrofit == null) {
            synchronized (RequestRetrofit.class) {
                if(retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.Server.BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit.create(service);
    }


}
