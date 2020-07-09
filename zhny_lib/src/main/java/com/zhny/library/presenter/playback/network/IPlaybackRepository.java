package com.zhny.library.presenter.playback.network;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.playback.model.LandInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.model.WorkDate;
import com.zhny.library.presenter.playback.model.WorkInfo;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * 回放
 */
public interface IPlaybackRepository {


    LiveData<BaseDto<List<PlaybackInfoDto>>> getDevices(String organizationId,String userId);

    LiveData<BaseDto<List<PlaybackMonthDetailDto>>> getDeviceDetail(String organizationId, String deviceSn);

    LiveData<BaseDto<List<TrackInfo>>> getTrackInfo(String organizationId, String deviceSn, String startDate, String endDate);

    LiveData<BaseDto<LandInfoDto>> getLandInfo(String organizationId, String username);

    LiveData<BaseDto<List<WorkInfo>>> getWorkInfo(String organizationId, String deviceSn);

    LiveData<BaseDto<List<WorkDate>>> getWorkDate(String organizationId, String deviceSn);


}
