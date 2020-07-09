package com.zhny.library.base;

import android.util.Log;

import com.zhny.library.exception.ApiException;
import com.zhny.library.exception.ExceptionEngine;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import androidx.lifecycle.MutableLiveData;


/**
 * created by liming
 */
public class ContentHttpSubscriber<T> implements Subscriber<T> {

    private static final String TAG = "ContentHttpSubscriber";

    private ApiException ex;

    private MutableLiveData<T> data;

    public MutableLiveData<T> get() {
        return data;
    }

    public void set(T t) {
        this.data.setValue(t);
    }

    public ContentHttpSubscriber() {
        data = new MutableLiveData();
    }

    public void onFinish(T t) {
        set(t);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
    }

    @Override
    public void onNext(T t) {
        try {
            onFinish(t);
        } catch (Exception e) {
            ex = ExceptionEngine.handleException(e);
            Log.e(TAG, "onNext >> "+  ex.getStatusCode() + " ==> " +  ex.getStatusDesc());
        }
    }

    @Override
    public void onError(Throwable t) {
        ex = ExceptionEngine.handleException(t);
        Log.e(TAG, "onError >> "+  ex.getStatusCode() + " ==> " +  ex.getStatusDesc());
    }

    @Override
    public void onComplete() {

    }

}
