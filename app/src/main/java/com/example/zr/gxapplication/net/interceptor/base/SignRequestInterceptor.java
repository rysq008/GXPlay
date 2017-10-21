package com.example.zr.gxapplication.net.interceptor.base;

import com.example.zr.gxapplication.utils.SignUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: zhou date: 2015/12/21.
 */
public abstract class SignRequestInterceptor<T> implements Interceptor {
  private T signKey;

  public SignRequestInterceptor(T signKey) {
    this.signKey = signKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Headers signedHeader = request.headers().newBuilder()
        .add(SignUtils.getSignHeaderName(), getSign(request, signKey)).build();
    Request signedRequest = request.newBuilder().headers(signedHeader).build();
    return chain.proceed(signedRequest);
  }

  protected abstract String getSign(Request request, T signKey);
}
