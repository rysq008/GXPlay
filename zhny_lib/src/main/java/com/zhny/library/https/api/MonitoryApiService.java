package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.monitor.model.dto.MachineProper;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 农机监控api
 */
public interface MonitoryApiService {

    /**
     * 获取农场地块信息
     *
     * @param organizationId    organizationId
     * @param userName          userName
     * @return                  result data
     */
    @GET("/asset/v1/{organizationId}/farm/farmInfo")
    Flowable<BaseDto<List<SelectFarmDto>>> getPlotData(@Path("organizationId") String organizationId,
                                                       @Query("userName") String userName);


    /**
     * 获取设备信息
     *
     * @param organizationId    organizationId
     * @param userId            userId
     * @return                  result data
     */
    @GET("/asset/v1/{organizationId}/devices/list")
    Flowable<BaseDto<List<SelectMachineDto>>> getDevices(@Path("organizationId") String organizationId,
                                                         @Query("userId") String userId);

    /**
     * 获取设备实时信息
     *
     * @param organizationId    organizationId
     * @param snList            snList eg('21,32,23')
     * @return                  result data
     */
    @GET("/asset/v1/{organizationId}/devices/properBySns")
    Flowable<BaseDto<List<MachineProper>>> getDevicesPropers(@Path("organizationId") String organizationId,
                                                             @Query("snList") String snList,
                                                             @Query("date") String date);


}
