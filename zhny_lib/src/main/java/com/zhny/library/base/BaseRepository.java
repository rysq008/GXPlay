package com.zhny.library.base;


import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Repository基类
 *
 * @Time 2019-07-21
 */

public class BaseRepository {

    /**
     * 请求网络
     *
     * @param flowable  flowable
     * @param <T>       dto
     * @return          result
     */
    public <T> BaseHttpSubscriber<T> request(Flowable<BaseDto<T>> flowable){
        BaseHttpSubscriber<T> baseHttpSubscriber = new BaseHttpSubscriber<>(); //RxJava Subscriber回调
        flowable.subscribeOn(Schedulers.io()) //解决背压
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(baseHttpSubscriber);
        return baseHttpSubscriber;
    }

    /**
     * 请求网络
     *
     * @param flowable  flowable
     * @param <T>       dto
     * @return          result
     */
    public <T> ContentHttpSubscriber<T> requestNoContent(Flowable<T> flowable){
        ContentHttpSubscriber<T> contentHttpSubscriber = new ContentHttpSubscriber<>(); //RxJava Subscriber回调
        flowable.subscribeOn(Schedulers.io()) //解决背压
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contentHttpSubscriber);
        return contentHttpSubscriber;
    }
}
