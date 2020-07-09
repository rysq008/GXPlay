package com.zhny.library.presenter.playback.network.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.api.PlayBackApiService;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.playback.model.LandInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.model.WorkDate;
import com.zhny.library.presenter.playback.model.WorkInfo;
import com.zhny.library.presenter.playback.network.IPlaybackRepository;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 *
 */
public class PlaybackRepository extends BaseRepository implements IPlaybackRepository {

    private Context context;
    private LoadingDialog dialog;

    public PlaybackRepository(LoadingDialog dialog, Context context) {
        this.context = context;
        this.dialog = dialog;
    }


    @Override
    public LiveData<BaseDto<List<PlaybackInfoDto>>> getDevices(
            String organizationId, String userId) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getDevices(organizationId,userId,1)).get();
    }

    @Override
    public LiveData<BaseDto<List<PlaybackMonthDetailDto>>> getDeviceDetail(
            String organizationId, String deviceSn) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getDeviceDetail(organizationId,deviceSn)).get();

    }

    @Override
    public LiveData<BaseDto<List<TrackInfo>>> getTrackInfo(
            String organizationId, String deviceSn, String startDate, String endDate) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getTrackinfo(organizationId,deviceSn,startDate,endDate)).get();
    }

    @Override
    public LiveData<BaseDto<LandInfoDto>> getLandInfo(String organizationId, String username) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getLandInfo(organizationId,username)).get();
    }

    @Override
    public LiveData<BaseDto<List<WorkInfo>>> getWorkInfo(
            String organizationId, String deviceSn) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getWorkInfo(organizationId,deviceSn)).get();
    }

    @Override
    public LiveData<BaseDto<List<WorkDate>>> getWorkDate(
            String organizationId, String deviceSn) {
        return request(RequestRetrofit.getInstance(context, dialog, PlayBackApiService.class).getWorkDate(organizationId,deviceSn)).get();
    }
}
