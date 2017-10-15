package com.example.zr.gxapplication.net.interceptor;

import android.text.TextUtils;

import com.example.zr.gxapplication.utils.ResponseBodyUtil;
import com.shandianshua.totoro.utils.net.ResponseBodyUtil;
import com.shandianshua.totoro.utils.net.SignUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * author: zhoulei date: 15/12/1.
 */
public class SignCheckInterceptor implements Interceptor {
  private final String signKey;

  public SignCheckInterceptor(String signKey) {
    this.signKey = signKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    String responseBody = ResponseBodyUtil.getResponseBody(response);
    if(response != null && response.isSuccessful()) {
      if (!TextUtils.isEmpty(responseBody)) {
        if (!SignUtils.checkSign(response.header(SignUtils.getSignHeaderName()), responseBody,
            signKey)) {
          throw new IOException("illegal response!");
        }
      }
    }
    return ResponseBodyUtil.recoverResponse(responseBody, response);
  }
}
