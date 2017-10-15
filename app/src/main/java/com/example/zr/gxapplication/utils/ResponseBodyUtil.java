package com.example.zr.gxapplication.utils;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import okhttp3.Response;
import okhttp3.ResponseBody;


public class ResponseBodyUtil {
  public static String getResponseBody(Response response) {
    try {
      return response.body().string();
    } catch (Throwable e) {
      return "";
    }
  }


  public static Response recoverResponse(byte[] responseBody, Response response) {
    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), responseBody)).build();
  }

  public static Response recoverResponse(String responseBody, Response response) {
    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), responseBody)).build();
  }
}
