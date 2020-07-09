package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.model.dto.MachineDto;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 机具列表api
 */
public interface MachineApiService {


    /**
     * 查询机具列表
     *
     * @param organizationId 组织id
     * @param userId         用户id
     * @return 机具列表
     */
    @GET("/asset/v1/{organizationId}/devices/list")
    Flowable<BaseDto<List<MachineDto>>> getMachines(@Path("organizationId") String organizationId,
                                                    @Query("userId") String userId);

    /**
     * 查询机具详情
     *
     * @param organizationId 组织id
     * @param deviceSn       设备sn编号
     * @return 机具详情
     */
    @GET("/asset/v1/{organizationId}/devices/info/{deviceSn}")
    Flowable<BaseDto<MachineDetailsDto>> getMachineDetails(@Path("organizationId") String organizationId,
                                                           @Path("deviceSn") String deviceSn);

}
