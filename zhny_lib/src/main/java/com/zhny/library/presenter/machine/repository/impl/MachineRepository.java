package com.zhny.library.presenter.machine.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.https.api.MachineApiService;
import com.zhny.library.presenter.machine.repository.IMachineRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;


public class MachineRepository extends BaseRepository implements IMachineRepository {

    private Context context;
    private LoadingDialog dialog;

    public MachineRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<List<MachineDto>>> getMachines(String organizationId, String userId) {
        Flowable<BaseDto<List<MachineDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, MachineApiService.class)
                .getMachines(organizationId, userId);
        return request(flowable).get();
    }


    @Override
    public LiveData<BaseDto<MachineDetailsDto>> getMachineDetails(String organizationId
            , String deviceSn) {
        Flowable<BaseDto<MachineDetailsDto>> flowable = RequestRetrofit
                .getInstance(context, dialog, MachineApiService.class)
                .getMachineDetails(organizationId, deviceSn);
        return request(flowable).get();
    }
}
