package com.example.zr.gxapplication.net;

import com.shandianshua.totoro.data.cache.Cache;
import com.shandianshua.totoro.data.cache.ObservableCache;
import com.shandianshua.totoro.utils.RxUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * author: zhou date: 2016/6/12.
 */
public class DataLoadHelper {
    public static <T> Observable<T> getFreshData(ObservableCache<T> cache, Observable<T> netOb) {
        if (null != cache && cache.isFresh()) {
            return cache.getOb();
        } else {
            return netOb;
        }
    }

    public static <T> Observable<T> getTwiceInjectOb(ObservableCache<T> cache, Observable<T> netOb) {
        if (null != cache && cache.isFresh()) {
            return cache.getOb();
        } else {
            return RxUtils.concatIgnoreErrorJustToast(cache.getOb(), netOb);
        }
    }

    public static <T> Observable<T> bindCacheUpdate(final Cache<T> cache, Observable<T> netOb) {
        return netOb.lift(new Observable.Operator<T, T>() {
            @Override
            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                return new CacheUpdateSubscriber<>(cache, subscriber);
            }
        });
    }

    private static class CacheUpdateSubscriber<T extends Cache<U>, U> extends Subscriber<U> {
        private Cache<U> mCache;
        private Subscriber<? super U> mSubscriber;

        public CacheUpdateSubscriber(T cache, Subscriber<? super U> subscriber) {
            mCache = cache;
            mSubscriber = subscriber;
        }


        @Override
        public void onCompleted() {
            mSubscriber.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            mSubscriber.onError(e);
        }

        @Override
        public void onNext(U u) {
            mCache.update(u);
            mSubscriber.onNext(u);
        }
    }
}
