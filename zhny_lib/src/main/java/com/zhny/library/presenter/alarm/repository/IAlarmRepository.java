package com.zhny.library.presenter.alarm.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.alarm.dto.AlarmDto;

import java.util.List;

import androidx.lifecycle.LiveData;


public interface IAlarmRepository {

    LiveData<BaseDto<List<AlarmDto>>> getAlarms(String organizationId, Boolean isDel, String userId);


    LiveData<BaseDto<Boolean>> batchRead(String organizationId, List<Integer> msgIds);

}
