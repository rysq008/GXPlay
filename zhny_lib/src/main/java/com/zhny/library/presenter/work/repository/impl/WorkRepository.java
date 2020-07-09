package com.zhny.library.presenter.work.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.dto.LandDto;
import com.zhny.library.presenter.work.dto.PictureDto;
import com.zhny.library.presenter.work.dto.SinglePicture;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.dto.WorkTrackDto;
import com.zhny.library.https.api.WorkApiService;
import com.zhny.library.presenter.work.repository.IWorkRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;


public class WorkRepository extends BaseRepository implements IWorkRepository {

    private Context context;
    private LoadingDialog dialog;

    public WorkRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }


    @Override
    public LiveData<BaseDto<List<SelectFarmDto>>> getFarms(String organizationId, String userName) {
        Flowable<BaseDto<List<SelectFarmDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getFarms(organizationId, userName);
        return request(flowable).get();
    }


    @Override
    public LiveData<BaseDto<List<WorkDto>>> getWorks(String organizationId, String fieldCode) {
        Flowable<BaseDto<List<WorkDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getWorks(organizationId, fieldCode);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<WorkDto>>> getOneYearWorks(String organizationId, String fieldCode, String date) {
        Flowable<BaseDto<List<WorkDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getOneYearWorks(organizationId, fieldCode, date);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<WorkTrackDto>>> getWorkTrack(String organizationId, String fieldCode, String jobType,
                                                              String startDate, String endDate) {
        Flowable<BaseDto<List<WorkTrackDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getWorkTrack(organizationId, fieldCode, jobType,startDate,endDate);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<PictureDto>>> getPictureData(String organizationId, String fieldCode, String sns
            , String startDate, String endDate, String jobType) {
        Flowable<BaseDto<List<PictureDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getPictureData(organizationId, fieldCode, sns,startDate,endDate,jobType);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<LandDto>> getLand(String organizationId, String username, String fieldName) {
        Flowable<BaseDto<LandDto>>  flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getLand(organizationId, username,fieldName);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<SinglePicture>>> getPictureDataBySn(String organizationId, String sns, String startTime, String endTime) {
        Flowable<BaseDto<List<SinglePicture>>>  flowable = RequestRetrofit
                .getInstance(context, dialog, WorkApiService.class)
                .getPictureDataBySn(organizationId, sns, startTime, endTime);
        return request(flowable).get();
    }
}
