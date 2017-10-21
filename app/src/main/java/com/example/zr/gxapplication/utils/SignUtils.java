package com.example.zr.gxapplication.utils;

import android.text.TextUtils;

import com.shandianshua.base.utils.HmacUtils;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Request;

/**
 * author: zhoulei date: 15/12/1.
 */
public class SignUtils {
  private static final String SIGN_HEADER_NAME = "Entei-Content";
  private static final String DEFAULT_SIGN_METHOD = "HMAC_MD5";
  protected static final String SIGN_DIVIDER = " ";

  public static String getSignHeaderName() {
    return SIGN_HEADER_NAME;
  }

  public static String getSignV1(Request request, String signKey) {
    String message = "";
    if (RequestBodyUtil.hasRequestBody(request)) {
      List<String> values = new LinkedList<>();
      String body;
      try {
        body = RequestBodyUtil.readBodyString(request);
      } catch (IOException e) {
        body = "";
      }
      try {
        body = URLDecoder.decode(body, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        body = "";
      }

      String[] keyValueItems = body.split("&");
      if (keyValueItems.length > 0) {
        for (String keyValueItem : keyValueItems) {
          String[] keyValuePair = keyValueItem.split("=");
          if (keyValuePair.length >= 2) {
            values.add(keyValuePair[1]);
          }
        }
      }
      StringBuilder valuesString = new StringBuilder();
      if (values.size() > 0) {
        Collections.sort(values);
        for (String value : values) {
          valuesString.append(value);
        }
      }
      message = valuesString.toString();
    }
    return DEFAULT_SIGN_METHOD + SIGN_DIVIDER + HmacUtils.hmacDigest(message,
        signKey, HmacUtils.Algorithm.HmacMD5);
  }

  public static String getSignV2(Request request, String signKey) {
    StringBuilder signSrc = new StringBuilder();
    signSrc.append(request.method());
    signSrc.append(request.url());
    byte[] requestBody = null;
    try {
      if (RequestBodyUtil.hasRequestBody(request)) {
        requestBody = RequestBodyUtil.readBody(request);
        signSrc.append(new String(requestBody, 0, requestBody.length, "utf-8"));
      }
    } catch (Throwable e) {
      // no request body, ignore
    }
    return DEFAULT_SIGN_METHOD + SIGN_DIVIDER + HmacUtils.hmacDigest(signSrc.toString(),
        signKey, HmacUtils.Algorithm.HmacMD5);
  }

  public static boolean checkSign(String enteiHeader, String body, String signKey) {
    if (TextUtils.isEmpty(enteiHeader)) {
      return false;
    }
    String[] signArray = enteiHeader.split(SIGN_DIVIDER);
    if (signArray.length != 2) {
      return false;
    }
    String responseSign = signArray[1];
    String rightSign =
        HmacUtils.hmacDigest(body, signKey, HmacUtils.Algorithm.HmacMD5);
    return rightSign.equals(responseSign);
  }
}
