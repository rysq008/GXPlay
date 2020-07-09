package com.zhny.library.presenter.work.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.work.dto.LandDto;
import com.zhny.library.presenter.work.dto.PictureDto;
import com.zhny.library.presenter.work.dto.WorkTrackDto;
import com.zhny.library.presenter.work.repository.IWorkRepository;
import com.zhny.library.presenter.work.repository.impl.WorkRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkTrackViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> showPicture = new MutableLiveData<>();
    public MutableLiveData<Boolean> showWidth = new MutableLiveData<>();
    public MutableLiveData<String> jobType = new MutableLiveData<>(); //作业类型
    public MutableLiveData<Boolean> isPloughing = new MutableLiveData<>(); //是否为犁地作业
    public MutableLiveData<String> standardValue = new MutableLiveData<>();//准确率
    public MutableLiveData<String> accuracyRate = new MutableLiveData<>();//标准值
    public MutableLiveData<String> floatValue  = new MutableLiveData<>(); //浮动值
    public MutableLiveData<String> width = new MutableLiveData<>(); //幅宽

    //请求作业轨迹 数据
    public LiveData<BaseDto<List<WorkTrackDto>>> getWorkTrack(String organizationId, String fieldCode, String jobType,
                                                              String startDate, String endDate) {
        IWorkRepository workRepository = new WorkRepository(null, context);
        return workRepository.getWorkTrack(organizationId,fieldCode,jobType,startDate,endDate);
    }

    //请求作业轨迹上图片 数据
    public LiveData<BaseDto<List<PictureDto>>> getPictureData(String fieldCode, String sns,
                                                              String startDate, String endDate, String jobType) {
        IWorkRepository workRepository = new WorkRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        return workRepository.getPictureData(organizationId,fieldCode,sns,startDate,endDate,jobType);
    }

    //请求地块 轨迹信息
    public LiveData<BaseDto<LandDto>> getLand(String organizationId, String username, String fieldName){
        IWorkRepository workRepository = new WorkRepository(null, context);
        return workRepository.getLand(organizationId,username,fieldName);
    }



}
