package com.zhny.zhny_app.event;

import io.reactivex.Flowable;

/**
 * Created by wanglei on 2016/12/22.
 */

public interface RxIBus<T> {

    void register(Object object);

    void unregister(Object object);

    void postEvent(Object obj);

    <T>Flowable<T> receiveEvent(Class<T> clz);

}
