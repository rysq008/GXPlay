package com.zhny.library.https.api;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.playback.model.LandInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackInfoDto;
import com.zhny.library.presenter.playback.model.PlaybackMonthDetailDto;
import com.zhny.library.presenter.playback.model.TrackInfo;
import com.zhny.library.presenter.playback.model.WorkDate;
import com.zhny.library.presenter.playback.model.WorkInfo;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 轨迹回放api
 */
public interface PlayBackApiService {



    /**
     * 设备列表
     */
    @GET("/asset/v1/{organizationId}/devices/list")
    Flowable<BaseDto<List<PlaybackInfoDto>>> getDevices(@Path("organizationId") String organizationId,
                                                        @Query("userId") String userId,
                                                        @Query("productCategory") int productCategory);

    /**
     * 设备详情
     */
    @GET("/asset/v1/{organizationId}/devices/info/{deviceSn}")
    Flowable<BaseDto<List<PlaybackMonthDetailDto>>> getDeviceDetail(@Path("organizationId") String organizationId,
                                                                    @Path("deviceSn") String deviceSn);

    /**
     * 查询设备轨迹信息
     */
    @GET("/asset/v1/{organizationId}/devices/track-info")
    Flowable<BaseDto<List<TrackInfo>>> getTrackinfo(@Path("organizationId") String organizationId,
                                                    @Query("deviceSn") String deviceSn,
                                                    @Query("startDate") String startDate,
                                                    @Query("endDate") String endDate);

    /**
     * 查询所有地块
     */
    @GET("/asset/v1/{organizationId}/farm/fieldInfo?pageSize=10000")
    Flowable<BaseDto<LandInfoDto>> getLandInfo(@Path("organizationId") String organizationId,
                                               @Query("userName") String userName);


    /**
     * 查询作业信息
     */
    @GET("/asset/v1/{organizationId}/devices/work-info")
    Flowable<BaseDto<List<WorkInfo>>> getWorkInfo(@Path("organizationId") String organizationId,
                                                  @Query("deviceSn") String deviceSn);

    /**
     * 查询作业信息(一台设备的所有作业信息)
     */
    @GET("/asset/v1/{organizationId}/devices/summaryList")
    Flowable<BaseDto<List<WorkDate>>> getWorkDate(@Path("organizationId") String organizationId,
                                                  @Query("snList") String deviceSn);
}
