package com.example.zr.gxapplication.net.interceptor;

import android.content.Context;
import android.support.annotation.Nullable;

import com.shandianshua.base.config.GlobalConfig;
import com.shandianshua.base.factory.GsonFactory;
import com.shandianshua.base.utils.CollectionUtils;
import com.shandianshua.base.utils.MD5Utils;
import com.shandianshua.base.utils.MainThreadPostUtils;
import com.shandianshua.base.utils.NetworkUtil;
import com.shandianshua.storage.GsonObjStorage;
import com.shandianshua.storage.Storage;
import com.shandianshua.totoro.utils.net.RequestBodyUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * a interceptor for okHttp, can retry request until request is successful
 *
 * author: zhou date: 2016/4/11.
 */
public class RetryRequestInterceptor implements Interceptor {
  private final RetryConfig retryConfig;
  private final GsonObjStorage<RequestWrapper> requestStorage;
  private final FutureTask<Void> initFuture;
  private final OkHttpClient okHttpClient;
  private static final String KEY_SUFFIX_SENDING_QUEST = ".sending";

  private long preRetryTime;

  private final ExecutorService threadPool = Executors.newSingleThreadExecutor();

  public RetryRequestInterceptor(Context context, RetryConfig retryConfig) {
    if (context == null) {
      throw new IllegalArgumentException("context can't be null");
    }
    if (retryConfig == null) {
      throw new IllegalArgumentException("retryConfig can not be null");
    }
    final Storage storage = retryConfig.storage();
    if (storage == null) {
      throw new IllegalArgumentException("retryConfig.storage() can not return null");
    }
    okHttpClient = retryConfig.okHttpClient();
    if (okHttpClient == null) {
      throw new IllegalArgumentException("okHttpClient can not be null");
    }
    this.retryConfig = retryConfig;
    this.requestStorage = new GsonObjStorage<>(RequestWrapper.class, storage);

    initFuture = new FutureTask<>(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        recoverSendingRequest();
        clearDirtyData();
        return null;
      }
    });
    threadPool.execute(initFuture);
  }

  public interface RetryResultListener {
    void onRetrySuccess(Request request, Response response);
    void onRetryError(Request request, IOException exception);
    void onRetryFailed(Request request, Response response);
    void onAbortRetry(Request request, long deadLine, int retryTimes);
  }

  public interface RetryConfig {
    int LIFE_FOREVER = -1;
    int UNLIMIT_RETRY_TIMES = -1;

    /**
     * min duration for retry request
     *
     * @return duration in unix times
     */
    long minRetryDuration();

    /**
     * life for retry request, if life < 0, will forever live
     *
     * @return life in unix time
     */
    long life();

    /**
     * max retry times of request, if maxRetryTimes < 0, will retry unLimit times
     *
     * @return max retry times
     */
    int maxRetryTimes();

    /**
     * the storage to store retry requests
     *
     * @return storage
     */
    Storage storage();

    /**
     * the okHttpClient to send retry requests
     *
     * @return okHttpClient
     */
    OkHttpClient okHttpClient();

    /**
     * judge whether to retry request
     *
     * @return whether to retry request
     */
    boolean isRetryRequest(Request request);

    /**
     * provide RetryResultListener to listen retry result
     *
     * @return RetryResultListener
     */
    @Nullable
    RetryResultListener retryResultListener();
  }

  public void triggerRetry() {
    if(System.currentTimeMillis() - preRetryTime > retryConfig.minRetryDuration()
        && NetworkUtil.isNetworkConnected(GlobalConfig.getAppContext())) {
      threadPool.execute(new Runnable() {
        @Override
        public void run() {
          preRetryTime = System.currentTimeMillis();
          tryRetryRequest();
        }
      });
    }
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    try {
      initFuture.get();
      Request request = chain.request();
      if(!retryConfig.isRetryRequest(request)) {
        return chain.proceed(request);
      }
      try {
        Response response = chain.proceed(request);
        if (!response.isSuccessful()) {
          saveToStorage(new RequestWrapper(request, retryConfig.life(), retryConfig.maxRetryTimes()));
        }
        return response;
      } catch (IOException e) {
        saveToStorage(new RequestWrapper(request, retryConfig.life(), retryConfig.maxRetryTimes()));
        throw new IOException(e);
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return chain.proceed(chain.request());
    }
  }

  private void saveToStorage(RequestWrapper requestWrapper) {
    if (requestWrapper == null) {
      return;
    }
    synchronized (requestStorage) {
      requestStorage.put(requestWrapper.getMd5(), requestWrapper);
    }
  }

  private void tryRetryRequest() {
    List<RequestWrapper> requestWrappers = outRetryRequests();
    if(CollectionUtils.isEmpty(requestWrappers)) {
      return;
    }
    for(RequestWrapper requestWrapper : requestWrappers) {
      Response response = null;
      Request request = requestWrapper.toRequest();
      try {
        response = executeRequest(request);
      } catch (IOException e) {
        restoreSendingRequest(requestWrapper);
        notifyRetryError(request, e);
        continue;
      }
      if(response.isSuccessful()) {
        finishSendRequest(requestWrapper);
        notifyRetrySuccess(request, response);
      } else {
        restoreSendingRequest(requestWrapper);
        notifyRetryFailed(request, response);
      }
    }
  }

  private Response executeRequest(Request request) throws IOException {
    return okHttpClient.newCall(request).execute();
  }

  private List<RequestWrapper> outRetryRequests() {
    List<RequestWrapper> requestWrappers = new ArrayList<>();
    synchronized (requestStorage) {
      Set<String> keyset = requestStorage.getKeys();
      if (keyset.isEmpty()) {
        return requestWrappers;
      }
      for (String key : keyset) {
        if (isSendingRequest(key)) {
          continue;
        }
        RequestWrapper requestWrapper = requestStorage.getFirst(key);
        if (deleteIfDirty(key, requestWrapper)) {
          continue;
        }
        moveToSendingRequest(key, requestWrapper);
        requestWrappers.add(requestWrapper);
      }
    }
    return requestWrappers;
  }

  private void moveToSendingRequest(String key, RequestWrapper requestWrapper) {
    String sendingKey = key + KEY_SUFFIX_SENDING_QUEST;
    requestWrapper.currentRetryTimes += 1;
    synchronized (requestStorage) {
      requestStorage.delete(key);
      requestStorage.put(sendingKey, requestWrapper);
    }
  }

  private void finishSendRequest(RequestWrapper requestWrapper) {
    String sendingKey = requestWrapper.getMd5() + KEY_SUFFIX_SENDING_QUEST;
    synchronized (requestStorage) {
      requestStorage.delete(sendingKey);
    }
  }

  private void restoreSendingRequest(RequestWrapper requestWrapper) {
    String md5Key = requestWrapper.getMd5();
    String sendingKey = md5Key + KEY_SUFFIX_SENDING_QUEST;
    synchronized (requestStorage) {
      requestStorage.rename(sendingKey, md5Key);
    }
  }

  private void recoverSendingRequest() {
    synchronized (requestStorage) {
      Set<String> keySet = requestStorage.getKeys();
      if (keySet.isEmpty()) {
        return;
      }
      for (String key : keySet) {
        if (key.endsWith(KEY_SUFFIX_SENDING_QUEST)) {
          requestStorage.put(convertSendingKeyToMd5Key(key),
              requestStorage.getFirst(key));
          requestStorage.delete(key);
        }
      }
    }
  }

  private void clearDirtyData() {
    synchronized (requestStorage) {
      Set<String> md5KeySet = requestStorage.getKeys();
      if (md5KeySet.isEmpty()) {
        return;
      }
      for (String key : md5KeySet) {
        RequestWrapper requestWrapper = requestStorage.getFirst(key);
        deleteIfDirty(key, requestWrapper);
      }
    }
  }

  private boolean isSendingRequest(String key) {
    return key != null && key.endsWith(KEY_SUFFIX_SENDING_QUEST);
  }

  private boolean isDirtyData(String key, RequestWrapper requestWrapper) {
    if (requestWrapper == null || requestWrapper.isDead()) {
      return true;
    }
    String md5Key;
    if (isSendingRequest(key)) {
      md5Key = convertSendingKeyToMd5Key(key);
    } else {
      md5Key = key;
    }
    return !requestWrapper.getMd5().equals(md5Key);
  }

  private boolean deleteIfDirty(String key, RequestWrapper requestWrapper) {
    if(isDirtyData(key, requestWrapper)) {
      requestStorage.delete(key);
      if(requestWrapper != null) {
        notifyRetryAbort(requestWrapper.toRequest(), requestWrapper.deadLine, requestWrapper.currentRetryTimes - 1);
      }
      return true;
    }
    return false;
  }

  private String convertSendingKeyToMd5Key(String sendingKey) {
    return sendingKey.substring(0, sendingKey.length() - KEY_SUFFIX_SENDING_QUEST.length());
  }

  private void notifyRetrySuccess(final Request request, final Response response) {
    final RetryResultListener retryResultListener = retryConfig.retryResultListener();
    if(retryResultListener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          retryResultListener.onRetrySuccess(request, response);
        }
      });
    }
  }

  private void notifyRetryError(final Request request, final IOException exception) {
    final RetryResultListener retryResultListener = retryConfig.retryResultListener();
    if(retryResultListener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          retryResultListener.onRetryError(request, exception);
        }
      });
    }
  }

  private void notifyRetryFailed(final Request request, final Response response) {
    final RetryResultListener retryResultListener = retryConfig.retryResultListener();
    if(retryResultListener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          retryResultListener.onRetryFailed(request, response);
        }
      });
    }
  }

  private void notifyRetryAbort(final Request request, final long deadLine, final int retryTimes) {
    final RetryResultListener retryResultListener = retryConfig.retryResultListener();
    if(retryResultListener != null) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          retryResultListener.onAbortRetry(request, deadLine, retryTimes);
        }
      });
    }
  }

  private static class RequestWrapper implements Serializable {
    private String url;
    private String method;
    private Map<String, List<String>> headers;
    private String mediaType;
    private byte[] body;
    private long deadLine;
    private int currentRetryTimes;
    private int maxRetryTimes;

    public RequestWrapper(Request request, long life, int maxRetryTimes) throws IOException {
      url = request.urlString();
      method = request.method();
      headers = request.headers().toMultimap();
      if (RequestBodyUtil.hasRequestBody(request)) {
        try {
          mediaType = request.body().contentType().toString();
          body = RequestBodyUtil.readBody(request);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if(life < 0) {
        deadLine = -1;
      } else {
        deadLine = System.currentTimeMillis() + life;
      }
      this.maxRetryTimes = maxRetryTimes;
    }

    public Request toRequest() {
      Request.Builder builder = new Request.Builder();
      builder.url(url)
          .method(method, body == null ? null : RequestBody.create(MediaType.parse(mediaType), body));
      if(headers.size() > 0) {
        for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
          for(String value : entry.getValue()) {
            builder.addHeader(entry.getKey(), value);
          }
        }
      }
      return builder.build();
    }

    public String getMd5() {
      StringBuilder srcBuilder = new StringBuilder();
      srcBuilder.append("url:").append(url)
          .append("method:").append(method)
          .append("headers:").append(GsonFactory.getGson().toJson(headers))
          .append("deadLine:").append(deadLine);
      if (body != null) {
        srcBuilder.append("mediaType:").append(mediaType)
            .append("body:").append(Arrays.toString(body));
      }
      return MD5Utils.MD5(srcBuilder.toString());
    }

    public boolean isDead() {
      return (deadLine >= 0 && System.currentTimeMillis() > deadLine)
           || (maxRetryTimes >= 0 && currentRetryTimes > maxRetryTimes);
    }
  }
}
