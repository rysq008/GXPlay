package com.zhny.library.base;

import com.zhny.library.common.Constant;
import com.zhny.library.exception.ApiException;
import com.zhny.library.exception.ExceptionEngine;
import com.zhny.library.exception.ServerException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import androidx.lifecycle.MutableLiveData;

/**
 * 自定义请服务器被观察者
 *
 * @Time 2019-07-21
 */
public class BaseHttpSubscriber<T> implements Subscriber<BaseDto<T>> {

    //异常类
    private ApiException ex;

    public BaseHttpSubscriber() {
        data = new MutableLiveData();
    }

    private MutableLiveData<BaseDto<T>> data;

    public MutableLiveData<BaseDto<T>> get() {
        return data;
    }

    public void set(BaseDto<T> t) {
        this.data.setValue(t);
    }

    public void onFinish(BaseDto<T> t) {
        set(t);
    }

    @Override
    public void onSubscribe(Subscription s) {
        // 观察者接收事件 = 1个
        s.request(1);
    }

    @Override
    public void onNext(BaseDto<T> t) {
        if (t.getMsgCode().equals(Constant.RespCode.R000)) {
//            Log.v("[BaseResponse]", "base response: " + t.getContent());
            onFinish(t);
        } else{
            ex = ExceptionEngine.handleException(new ServerException(t.getMsgCode(), t.getMsg()));
            getErrorDto(ex);
        }
    }

    @Override
    public void onError(Throwable t) {
        ex = ExceptionEngine.handleException(t);
        getErrorDto(ex);
    }

    /**
     * 初始化错误的dto
     */
    private void getErrorDto(ApiException ex) {
        BaseDto dto = new BaseDto();
        dto.setMsgCode(ex.getStatusCode());
        dto.setMsg(ex.getStatusDesc());
        onFinish((BaseDto<T>) dto);
    }

    @Override
    public void onComplete() {
    }

}
