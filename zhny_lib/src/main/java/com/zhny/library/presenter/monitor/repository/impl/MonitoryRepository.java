package com.zhny.library.presenter.monitor.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.api.LoginApiService;
import com.zhny.library.https.api.MonitoryApiService;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.monitor.model.dto.MachineProper;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;
import com.zhny.library.presenter.monitor.repository.IMonitoryRepository;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;

/**
 * created by liming
 */
public class MonitoryRepository extends BaseRepository implements IMonitoryRepository {

    private Context context;
    private LoadingDialog dialog;

    public MonitoryRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<List<SelectFarmDto>>> getPlotData(String organizationId, String userName) {
        Flowable<BaseDto<List<SelectFarmDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, MonitoryApiService.class)
                .getPlotData(organizationId, userName);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<SelectMachineDto>>> getDevices(String organizationId, String userId) {
        Flowable<BaseDto<List<SelectMachineDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, MonitoryApiService.class)
                .getDevices(organizationId, userId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<MachineProper>>> getDevicesPropers(String organizationId, String snList, String date) {
        Flowable<BaseDto<List<MachineProper>>> flowable = RequestRetrofit
                .getInstance(context, dialog, MonitoryApiService.class)
                .getDevicesPropers(organizationId, snList, date);
        return request(flowable).get();
    }


    @Override
    public LiveData<UserInfoDto> getUserInfo(){
        return requestNoContent(RequestRetrofit.getInstance(context, dialog, LoginApiService.class).getUserInfo()).get();
    }

    @Override
    public LiveData<TokenInfoDto> getToken(Map<String, String> params) {
        return requestNoContent(RequestRetrofit.getInstance(context, dialog, LoginApiService.class).getToken(params)).get();

    }

}
