package com.zhny.library.presenter.driver.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.retrofit.RequestRetrofit;

import com.zhny.library.presenter.driver.model.dto.DriverDto;

import com.zhny.library.https.api.DriverApiService;
import com.zhny.library.presenter.driver.repository.IDriverRepository;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import okhttp3.RequestBody;


public class DriverRepository extends BaseRepository implements IDriverRepository {

    private Context context;
    private LoadingDialog dialog;

    public DriverRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<List<DriverDto>>> getDrivers(String organizationId) {
        Flowable<BaseDto<List<DriverDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, DriverApiService.class)
                .getDrivers(organizationId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Boolean>> saveDriver(String organizationId, RequestBody requestBody) {
        return request(RequestRetrofit.getInstance(context, dialog, DriverApiService.class)
                .saveDriver(organizationId, requestBody)).get();
    }
}
