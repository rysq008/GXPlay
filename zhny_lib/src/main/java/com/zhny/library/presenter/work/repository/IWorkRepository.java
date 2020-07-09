package com.zhny.library.presenter.work.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.dto.LandDto;
import com.zhny.library.presenter.work.dto.PictureDto;
import com.zhny.library.presenter.work.dto.SinglePicture;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.dto.WorkTrackDto;

import java.util.List;

import androidx.lifecycle.LiveData;


public interface IWorkRepository {

    LiveData<BaseDto<List<SelectFarmDto>>> getFarms(String organizationId, String userName);

    LiveData<BaseDto<List<WorkDto>>> getWorks(String organizationId, String fieldCode);

    LiveData<BaseDto<List<WorkDto>>> getOneYearWorks(String organizationId, String fieldCode, String date);

    LiveData<BaseDto<List<WorkTrackDto>>> getWorkTrack(String organizationId, String fieldCode, String jobType,
                                                       String startDate, String endDate);

    LiveData<BaseDto<List<PictureDto>>> getPictureData(String organizationId, String fieldCode, String sns,
                                                       String startDate, String endDate, String jobType);

    LiveData<BaseDto<LandDto>> getLand(String organizationId, String username, String fieldName);

    LiveData<BaseDto<List<SinglePicture>>> getPictureDataBySn(String organizationId, String sns,
                                                              String startTime, String endTime);
}
