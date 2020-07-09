package com.zhny.library.presenter.alarm.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.alarm.dto.AlarmDto;
import com.zhny.library.presenter.alarm.repository.IAlarmRepository;
import com.zhny.library.presenter.alarm.repository.impl.AlarmRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> emptyMag= new MutableLiveData<>(); //没有机手


    //请求报警消息列表 数据
    public LiveData<BaseDto<List<AlarmDto>>> getAlarms() {
        IAlarmRepository alarmRepository = new AlarmRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        String userId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        return alarmRepository.getAlarms(organizationId, false, userId);
    }

    //批量阅读
    public void batchRead(List<Integer> msgIds) {
        IAlarmRepository alarmRepository = new AlarmRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        alarmRepository.batchRead(organizationId, msgIds);
    }

}
