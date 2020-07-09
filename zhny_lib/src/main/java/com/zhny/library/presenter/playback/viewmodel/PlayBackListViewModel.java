package com.zhny.library.presenter.playback.viewmodel;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.network.IPlaybackRepository;
import com.zhny.library.presenter.playback.network.impl.PlaybackRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * author : shd
 * date : 2020-02-07 09:53
 */
public class PlayBackListViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public LiveData<BaseDto<List<PlaybackInfoDto>>> getDevices(String organizationId, String userId){
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        return playbackRepository.getDevices(organizationId,userId);
    }

    public LiveData<BaseDto<List<PlaybackMonthDetailDto>>> getDeviceDetail(String organizationId, String deviceSn){
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        return playbackRepository.getDeviceDetail(organizationId,deviceSn);
    }

}
