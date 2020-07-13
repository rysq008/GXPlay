package com.zhny.zhny_app.event;

/**
 * Created by wanglei on 2016/12/22.
 */

public interface IBus {

    void register(Object object);

    void unregister(Object object);

    void post(IEvent event);

    void postSticky(IEvent event);

    void postEvent(Object obj);

    interface IEvent {
        int getTag();
    }

}
