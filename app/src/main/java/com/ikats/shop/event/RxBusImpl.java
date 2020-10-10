package com.ikats.shop.event;

import androidx.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class RxBusImpl {

    private FlowableProcessor<Object> bus = null;

    private RxBusImpl() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBusImpl get() {
        return Holder.instance;
    }


    public void register(Object object) {
    }


    public void unregister(Object object) {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        bus.onComplete();
    }

    public void postEvent(@NonNull Object obj) {
        bus.onNext(obj);
    }

    public <T> Flowable<T> receiveEvent(Class<T> clz) {
        return receiveEvent(clz, AndroidSchedulers.mainThread());
    }

    public <T> Flowable<T> receiveEvent(final Class<T> clz, final Scheduler scheduler) {
        Flowable<T> flowable = bus.ofType(clz).onBackpressureBuffer();
        if (scheduler != null) {
            return flowable.observeOn(scheduler);
        }
        return flowable;
    }

    public boolean hasSubscribers() {
        return bus.hasSubscribers();
    }

    private static class Holder {
        private static final RxBusImpl instance = new RxBusImpl();
    }
}
