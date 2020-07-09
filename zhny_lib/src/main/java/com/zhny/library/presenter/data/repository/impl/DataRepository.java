package com.zhny.library.presenter.data.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.api.DataApiService;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.DataStatisticsDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.repository.IDataRepository;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;


public class DataRepository extends BaseRepository implements IDataRepository {

    private Context context;
    private LoadingDialog dialog;

    public DataRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    @Override
    public LiveData<BaseDto<DataStatisticsDto>> getDataStatistics(String organizationId) {
        Flowable<BaseDto<DataStatisticsDto>> flowable = RequestRetrofit
                .getInstance(context, dialog, DataApiService.class)
                .getDataStatistics(organizationId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<JobReportDto>> getJobReport(String organizationId,
                                                        String userId,
                                                        String startDate,
                                                        String endDate,
                                                        String snList,
                                                        boolean deviceFlag,
                                                        int sortRule) {
        Flowable<BaseDto<JobReportDto>> flowable = RequestRetrofit
                .getInstance(context, dialog, DataApiService.class)
                .getJobReport(organizationId, userId, startDate, endDate, snList, deviceFlag, sortRule);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<DataDeviceDto>>> getDataDevices(String organizationId, String userId) {
        Flowable<BaseDto<List<DataDeviceDto>>> flowable = RequestRetrofit
                .getInstance(context, dialog, DataApiService.class)
                .getDataDevices(organizationId, userId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<String>>> getWorkDate(String organizationId, String startDate, String endDate) {
        Flowable<BaseDto<List<String>>> flowable = RequestRetrofit
                .getInstance(context, dialog, DataApiService.class)
                .getWorkDate(organizationId, startDate, endDate);
        return request(flowable).get();
    }
}
