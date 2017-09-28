package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.base.config.GlobalConfig;
import com.shandianshua.base.utils.MainThreadPostUtils;
import com.shandianshua.base.utils.NetworkUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.Interceptor;

/**
 * author: zhoulei date: 15/12/7.
 */
public class ReadableErrorInterceptor implements Interceptor {

  private static final String NET_NO_CONNECT = "网络未连接";
  private static final String NET_BUSY = "系统繁忙,请稍候重试";

  private static final ReadableErrorInterceptor instance = new ReadableErrorInterceptor();

  private HttpErrorListener httpErrorListener;

  protected ReadableErrorInterceptor() {

  }

  public static ReadableErrorInterceptor getInstance() {
    return instance;
  }

  public void setHttpErrorListener(HttpErrorListener httpErrorListener) {
    this.httpErrorListener = httpErrorListener;
  }

  public interface HttpErrorListener {
    void onHttpFailed(Request request, Response response);

    void onHttpError(Request request, Throwable throwable);
  }

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    final Request request = chain.request();
    Response response = null;
    try {
      response = chain.proceed(request);
    } catch (Throwable t) {
      notifyHttpError(request, t);
      if (!NetworkUtil.isNetworkConnected(GlobalConfig.getAppContext())) {
        throw new IOException(NET_NO_CONNECT);
      }
      throw new IOException(t);
    }
    if (!response.isSuccessful()) {
      notifyHttpFailed(request, response);
      throw new IOException(buildReadableError(response));
    } else {
      return response;
    }
  }

  private String buildReadableError(Response response) {
    return response.code() == 500 ? NET_BUSY : "ERR_CODE:" + response.code() + " MSG:"
        + response.message();
  }

  protected void notifyHttpError(final Request request, final Throwable throwable) {
    final HttpErrorListener listener = httpErrorListener;
    if (listener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          listener.onHttpError(request, throwable);
        }
      });
    }
  }

  protected void notifyHttpFailed(final Request request, final Response response) {
    final HttpErrorListener listener = httpErrorListener;
    if (listener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          listener.onHttpFailed(request, response);
        }
      });
    }
  }

}
