package com.zhny.library.presenter.monitor.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.login.custom.LoadingDialog;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.repository.IMonitoryRepository;
import com.zhny.library.presenter.monitor.repository.impl.MonitoryRepository;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.network.IPlaybackRepository;
import com.zhny.library.presenter.playback.network.impl.PlaybackRepository;
import com.zhny.library.presenter.work.dto.SinglePicture;
import com.zhny.library.presenter.work.repository.IWorkRepository;
import com.zhny.library.presenter.work.repository.impl.WorkRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitoringViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> showPicture = new MutableLiveData<>();
    public MutableLiveData<Boolean> showWidth = new MutableLiveData<>();


    //请求轨迹数据
    public LiveData<BaseDto<List<TrackInfo>>> getTrackInfo(LoadingDialog dialog, String deviceSn, String startDate, String endDate) {
        IPlaybackRepository playbackRepository = new PlaybackRepository(dialog, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");

        return playbackRepository.getTrackInfo(organizationId, deviceSn, startDate, endDate);
    }


    //请求 地块 数据
    public LiveData<BaseDto<List<SelectFarmDto>>> getPlotData() {
        IMonitoryRepository monitoryRepository = new MonitoryRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userName = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USERNAME, "admin");
        return monitoryRepository.getPlotData(organizationId, userName);
    }


    //请求作业轨迹上图片 数据
    public LiveData<BaseDto<List<SinglePicture>>> getPictureData(String sns, String startTime, String endTime) {
        IWorkRepository workRepository = new WorkRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return workRepository.getPictureDataBySn(organizationId, sns, startTime, endTime);
    }


}
