package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.totoro.data.net.interceptor.base.SignRequestInterceptor;
import com.shandianshua.totoro.utils.net.SignUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

/**
 * author: zhou date: 2015/12/21.
 */
public class SignRequestV1Interceptor extends SignRequestInterceptor<String>implements Interceptor {
  public SignRequestV1Interceptor(String signKey) {
    super(signKey);
  }

  @Override
  protected String getSign(Request request, String signKey) {
    return SignUtils.getSignV1(request, signKey);
  }
}
