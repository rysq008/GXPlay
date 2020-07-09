package com.zhny.library.presenter.alarm.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.alarm.dto.AlarmDto;
import com.zhny.library.https.api.AlarmApiService;
import com.zhny.library.presenter.alarm.repository.IAlarmRepository;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;


public class AlarmRepository extends BaseRepository implements IAlarmRepository {

    private Context context;
    private LoadingDialog dialog;

    public AlarmRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<List<AlarmDto>>> getAlarms(String organizationId, Boolean isDel, String userId) {
        Flowable<BaseDto<List<AlarmDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, AlarmApiService.class)
                .getAlarms(organizationId, isDel, userId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Boolean>> batchRead(String organizationId, List<Integer> msgIds) {
        Flowable<BaseDto<Boolean>> flowable = RequestRetrofit
                .getInstance(context, dialog, AlarmApiService.class)
                .batchRead(organizationId, msgIds);
        return request(flowable).get();
    }

}
