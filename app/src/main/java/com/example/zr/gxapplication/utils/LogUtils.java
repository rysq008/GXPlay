package com.example.zr.gxapplication.utils;

import android.util.Log;

import com.avos.avoscloud.AVObject;
import com.shandianshua.base.utils.NetUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Date;

import okhttp3.Request;
import okhttp3.Response;


public class LogUtils {
  public static void sendHttpFailedLog(Request request, Response response) {
//    if (NetUtils.isConnected()) {
//      AVObject avObject = new AVObject("HttpErrorLog");
//      avObject.put("response", response.toString());
//      avObject.put("responseCode", String.valueOf(response.code()));
//      avObject.put("responseBody", ResponseBodyUtil.getResponseBody(response));
//      avObject.put("method", request.method());
//      avObject.put("url", request.urlString());
//      avObject.put("headers", request.headers().toString());
//      try {
//        avObject.put("requestBody", RequestBodyUtil.readBodyString(request));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//      avObject.saveInBackground();
//    }
  }

  public static void sendHttpExceptionLog(Request request, Throwable throwable) {
//    if (NetUtils.isConnected()) {
//      AVObject avObject = new AVObject("HttpExceptionLog");
//      avObject.put("method", request.method());
//      avObject.put("url", request.urlString());
//      avObject.put("headers", request.headers().toString());
//      try {
//        avObject.put("requestBody", RequestBodyUtil.readBodyString(request));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//      avObject.put("exception", throwable.toString());
//      StackTraceElement[] stackTraceElements = throwable.getStackTrace();
//      if (stackTraceElements != null && stackTraceElements.length > 0) {
//        StringBuilder stackTrace = new StringBuilder();
//        for (StackTraceElement stackTraceElement : stackTraceElements) {
//          stackTrace.append(stackTraceElement.toString()).append("\n");
//        }
//        avObject.put("stack", stackTrace.toString());
//      }
//      avObject.saveInBackground();
//    }
  }

  public static void sendRetryAbortLog(Request request, long deadLine, int retryTimes) {
//    if (NetUtils.isConnected()) {
//      AVObject avObject = new AVObject("AbortRetryRequest");
//      avObject.put("url", request.url());
//      avObject.put("method", request.method());
//      avObject.put("headers", request.headers().toString());
//      try {
//        if (RequestBodyUtil.hasRequestBody(request)) {
//          avObject.put("body", RequestBodyUtil.readBodyString(request));
//        }
//      } catch (IOException e) {
//        avObject.put("body", "read failed:" + e.getMessage());
//      }
//      if (deadLine > 0) {
//        avObject.put("deadLine", new Date(deadLine));
//      }
//      avObject.put("retryTimes", String.valueOf(retryTimes));
//      avObject.put("createdAt", new Date());
//      avObject.saveInBackground();
//    }
  }


  private static boolean logEnabled = false;

  public static void setLogEnabled(boolean enable) {
    logEnabled = enable;
  }

  public static boolean isLogEnabled() {
    return logEnabled;
  }

  public static void printException(Throwable e) {
    if (logEnabled) {
      e.printStackTrace();
    }
  }

  public static int i(String tag, String msg) {
    if (logEnabled) {
      return Log.i(tag, msg);
    } else {
      return 0;
    }
  }

  public static int d(String tag, String msg) {
    if (logEnabled) {
      return Log.d(tag, msg);
    } else {
      return 0;
    }
  }

  public static int v(String tag, String msg) {
    if (logEnabled) {
      return Log.v(tag, msg);
    } else {
      return 0;
    }
  }

  public static int w(String tag, String msg) {
    return Log.w(tag, msg);
  }

  public static int w(String tag, Throwable e) {
    if (logEnabled) {
      return Log.w(tag, e);
    } else {
      return 0;
    }
  }

  public static int w(String tag, String msg, Throwable e) {
    if (logEnabled) {
      return Log.w(tag, msg, e);
    } else {
      return 0;
    }
  }

  public static int e(String tag, String msg) {
    return Log.e(tag, msg);
  }

  public static int e(String tag, String msg, Throwable e) {
    if (logEnabled) {
      return Log.e(tag, msg, e);
    } else {
      return 0;
    }
  }

}
