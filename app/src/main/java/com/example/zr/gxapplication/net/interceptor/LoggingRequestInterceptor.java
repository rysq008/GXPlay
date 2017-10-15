package com.example.zr.gxapplication.net.interceptor;

import com.example.zr.gxapplication.utils.LogUtils;
import com.example.zr.gxapplication.utils.RequestBodyUtil;
import com.shandianshua.base.utils.LogUtils;
import com.shandianshua.totoro.utils.net.RequestBodyUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: zhoulei date: 15/11/23.
 */
public class LoggingRequestInterceptor implements Interceptor {
  private static final String TAG = LoggingRequestInterceptor.class.getSimpleName();
  private static final MediaType FORM_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");
  private static final LoggingRequestInterceptor instance = new LoggingRequestInterceptor();

  private LoggingRequestInterceptor() {

  }

  public static LoggingRequestInterceptor getInstance() {
    return instance;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    if (LogUtils.isLogEnabled()) {
      LogUtils.i(TAG, String.format("Execute request %s:%s on %s%n%s", request.method(),
          request.url(), chain.connection(), request.headers()));
      if (RequestBodyUtil.hasRequestBody(request)) {
        MediaType mediaType = request.body().contentType();
        LogUtils.i(TAG, "Request Body Content-type: " + mediaType.toString());

        String bodyString = RequestBodyUtil.readBodyString(request);
        String bodyLog;
        if(mediaType.equals(FORM_MEDIA_TYPE)) {
          bodyLog = URLDecoder.decode(bodyString, "UTF-8");
        } else {
          bodyLog = bodyString;
        }
        LogUtils.i(TAG, "Request Body: " + bodyLog);
      }
    }
    return chain.proceed(request);
  }
}
