package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.base.utils.LogUtils;
import com.shandianshua.totoro.utils.net.ResponseBodyUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * author: zhoulei date: 15/11/23.
 */
public class LoggingResponseInterceptor implements Interceptor {
  private static final String TAG = LoggingResponseInterceptor.class.getSimpleName();
  private static LoggingResponseInterceptor instance = new LoggingResponseInterceptor();

  private LoggingResponseInterceptor() {

  }

  public static LoggingResponseInterceptor getInstance() {
    return instance;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Response response = null;
    if (LogUtils.isLogEnabled()) {
      long t1 = System.nanoTime();
      try {
        response = chain.proceed(request);
      } catch (Throwable t) {
        LogUtils.i(TAG, "onExecuteError:" + t.toString());
        throw new IOException(t);
      }
      long t2 = System.nanoTime();
      LogUtils.i(TAG, String.format("Received response for %s in %.1fms%n%s",
          response.request().url(), (t2 - t1) / 1e6d, response.headers()));
      String body = ResponseBodyUtil.getResponseBody(response);
      LogUtils.i(TAG, "Response Body: " + body);
      return ResponseBodyUtil.recoverResponse(body, response);
    } else {
      response = chain.proceed(request);
    }
    return response;
  }
}
