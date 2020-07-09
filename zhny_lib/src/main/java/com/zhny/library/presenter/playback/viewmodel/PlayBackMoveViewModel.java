package com.zhny.library.presenter.playback.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.playback.model.LandInfoDto;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.network.IPlaybackRepository;
import com.zhny.library.presenter.playback.network.impl.PlaybackRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayBackMoveViewModel extends ViewModel {

    public MutableLiveData<Boolean> showWorkLayout = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRunLayout = new MutableLiveData<>();
    public MutableLiveData<Boolean> showOffLineLayout = new MutableLiveData<>();
    public MutableLiveData<Boolean> showLeftLayout = new MutableLiveData<>();
    public MutableLiveData<Boolean> showLeftName = new MutableLiveData<>();
    public MutableLiveData<String> workName = new MutableLiveData<>();
    public MutableLiveData<String> workSpeed = new MutableLiveData<>();

    public void setWorkSpeed(String value) {
        workSpeed.setValue(value);
    }

    public void setWorkName(String value) {
        workName.setValue(value);
    }

    public void showLeftName(boolean isShow) {
        showLeftName.setValue(isShow);
    }

    public void showLeftLayout(Boolean isShow) {
        Boolean value = showLeftLayout.getValue();
        if (!isShow.equals(value)) {
            showLeftLayout.setValue(isShow);
        }

    }

    public void showWorkLayout(boolean isShow) {
        showWorkLayout.setValue(isShow);
    }

    public void showRunLayout(boolean isShow) {
        showRunLayout.setValue(isShow);
    }

    public void showOffLineLayout(boolean isShow) {
        showOffLineLayout.setValue(isShow);
    }


    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public LiveData<BaseDto<List<TrackInfo>>> getTrackInfo(LoadingDialog dialog, String deviceSn, String startDate, String endDate) {
        IPlaybackRepository playbackRepository = new PlaybackRepository(dialog, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");

        return playbackRepository.getTrackInfo(organizationId, deviceSn, startDate, endDate);
    }

    public LiveData<BaseDto<LandInfoDto>> getLandInfo() {
        IPlaybackRepository playbackRepository = new PlaybackRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        String userName = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USERNAME, "");
        return playbackRepository.getLandInfo(organizationId, userName);
    }


}
