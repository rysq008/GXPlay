package com.zhny.library.presenter.monitor.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.monitor.model.dto.MachineProper;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;

/**
 * created by liming
 */
public interface IMonitoryRepository {

    /**
     * 获取农场地块信息
     *
     * @param organizationId    organizationId
     * @param userName          userName
     * @return                  result data
     */
    LiveData<BaseDto<List<SelectFarmDto>>> getPlotData(String organizationId, String userName);

    /**
     * 获取农机（设备）信息
     *
     * @param organizationId    organizationId
     * @param userId            userId
     * @return                  result data
     */
    LiveData<BaseDto<List<SelectMachineDto>>> getDevices(String organizationId, String userId);

    /**
     * 获取农机（设备）实时信息
     *
     * @param organizationId    organizationId
     * @param snList            snList
     * @return                  result data
     */
    LiveData<BaseDto<List<MachineProper>>> getDevicesPropers(String organizationId, String snList, String date);


    LiveData<UserInfoDto> getUserInfo();

    LiveData<TokenInfoDto> getToken(Map<String, String> params);


}

