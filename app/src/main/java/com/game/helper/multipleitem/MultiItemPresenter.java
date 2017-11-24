package com.game.helper.multipleitem;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription;
import io.reactivex.schedulers.Schedulers;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Author: Season(ssseasonnn@gmail.com)
 * Date: 2016/10/10
 * Time: 12:07
 * FIXME
 */
public class MultiItemPresenter {
    private static int count = -1;
    private ArrayCompositeSubscription mSubscriptions;
    private MultiItemView mView;
    private Context mContext;

    public MultiItemPresenter(Context context) {
        mContext = context;
        mSubscriptions = new ArrayCompositeSubscription(5);
    }

    public void setDataLoadCallBack(MultiItemView itemView) {
        mView = itemView;
    }

    void unsubscribeAll() {
        mSubscriptions.dispose();
    }

    public void loadData(final boolean isRefresh) {
        List<ItemType> mData = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            if (i % 2 == 0) {
                ItemType bean = new TypeOneBean("type one");
                mData.add(bean);
            } else {
                ItemType bean = new TypeTwoBean("type two");
                mData.add(bean);
            }
        }
        mView.onDataLoadSuccess(mData, isRefresh);
//
//        createObservable()
//                .subscribeOn(Schedulers.io())
//                .delay(2, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<ItemType>>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.w("SingleItemPresenter", e);
//                        mView.onDataLoadFailed(isRefresh);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<ItemType> list) {
//                        mView.onDataLoadSuccess(mData, isRefresh);
//                    }
//                });
    }

    private Observable<List<ItemType>> createObservable() {
        count++;
        count %= 6;
        return Observable.create(new ObservableOnSubscribe<List<ItemType>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ItemType>> subscriber) throws Exception {
                if (count == 3) {
                    subscriber.onError(new Throwable("on error"));
                    return;
                }
                if (count == 5) {
                    subscriber.onNext(new ArrayList<ItemType>());
                    return;
                }
                List<ItemType> mData = new ArrayList<>();
                for (int i = count * 5; i < count * 5 + 2; i++) {
                    if (i % 2 == 0) {
                        ItemType bean = new TypeOneBean("type one");
                        mData.add(bean);
                    } else {
                        ItemType bean = new TypeTwoBean("type two");
                        mData.add(bean);
                    }
                }
                subscriber.onNext(mData);
                subscriber.onComplete();
            }
        });
    }
}
