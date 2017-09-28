package com.example.zr.gxapplication.net.interceptor.base;

import com.shandianshua.totoro.utils.net.SignUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

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
