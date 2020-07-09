package com.zhny.library.presenter.playback.viewmodel;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.model.WorkDate;
import com.zhny.library.presenter.playback.model.WorkInfo;
import com.zhny.library.presenter.playback.network.IPlaybackRepository;
import com.zhny.library.presenter.playback.network.impl.PlaybackRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * description ： TODO:类的作用
 * author : shd
 * date : 2020-02-07 09:53
 */
public class PlayBackMonthDetailViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> isOpen = new MutableLiveData<>();
    public MutableLiveData<Boolean> emptyData = new MutableLiveData<>(); //没有数据


    public LiveData<BaseDto<List<PlaybackMonthDetailDto>>> getDeviceDetail(String organizationId, String deviceSn){
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        return playbackRepository.getDeviceDetail(organizationId,deviceSn);
    }

    public LiveData<BaseDto<List<WorkInfo>>> getWorkinfo(String organizationId, String deviceSn){
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        return playbackRepository.getWorkInfo(organizationId,deviceSn);
    }

    public LiveData<BaseDto<List<WorkDate>>> getWorkDate(String organizationId, String deviceSn){
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        return playbackRepository.getWorkDate(organizationId,deviceSn);
    }

}
