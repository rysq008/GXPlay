package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.totoro.data.net.interceptor.base.SignRequestInterceptor;
import com.shandianshua.totoro.utils.net.SignUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

/**
 * author: zhoulei date: 15/12/1.
 */
public class SignRequestV2Interceptor extends SignRequestInterceptor<String> implements Interceptor {
  public SignRequestV2Interceptor(String signKey) {
    super(signKey);
  }

  @Override
  protected String getSign(Request request, String signKey) {
    return SignUtils.getSignV2(request, signKey);
  }
}
