package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.dto.LandDto;
import com.zhny.library.presenter.work.dto.PictureDto;
import com.zhny.library.presenter.work.dto.SinglePicture;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.dto.WorkTrackDto;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * work api service
 */
public interface WorkApiService {


    /**
     * 根据用户名 查询用户的农场（农场中包含地块）
     *
     * @param userName 用户名
     * @return List<FarmJson>格式数据
     */
    @GET("/asset/v1/{organizationId}/farm/farmInfo")
    Flowable<BaseDto<List<SelectFarmDto>>> getFarms(@Path("organizationId") String organizationId,
                                                    @Query("userName") String userName);

    /**
     * 查询所有作业季
     *
     * @param fieldCode 地块id
     * @return 作业季
     */
    @GET("/asset/v1/{organizationId}/quality-analysis/work")
    Flowable<BaseDto<List<WorkDto>>> getWorks(@Path("organizationId") String organizationId,
                                              @Query("fieldCode") String fieldCode);

    /**
     * 查询 某年 作业季
     *
     * @param fieldCode 地块id
     * @param date      时间/年份
     * @return 作业季
     */
    @GET("/asset/v1/{organizationId}/quality-analysis/work")
    Flowable<BaseDto<List<WorkDto>>> getOneYearWorks(@Path("organizationId") String organizationId,
                                                     @Query("fieldCode") String fieldCode,
                                                     @Query("date") String date);


    /**
     * 查询 作业轨迹
     *
     * @param fieldCode 地块Id
     * @param jobType   作业类型
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return data
     */
    @GET("/asset/v1/{organizationId}/quality-analysis/track")
    Flowable<BaseDto<List<WorkTrackDto>>> getWorkTrack(@Path("organizationId") String organizationId,
                                                       @Query("fieldCode") String fieldCode,
                                                       @Query("jobType") String jobType,
                                                       @Query("startDate") String startDate,
                                                       @Query("endDate") String endDate);


    /**
     * 查询 作业轨迹图片
     *
     * @param fieldCode 地块Id
     * @param sns       SN
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return data
     */
    @GET("/asset/v1/{organizationId}/quality-analysis/images")
    Flowable<BaseDto<List<PictureDto>>> getPictureData(@Path("organizationId") String organizationId,
                                                       @Query("fieldCode") String fieldCode,
                                                       @Query("sns") String sns,
                                                       @Query("startDate") String startDate,
                                                       @Query("endDate") String endDate,
                                                       @Query("jobType") String jobType);

    /**
     * 查询单个地块信息
     */
    @GET("/asset/v1/{organizationId}/farm/fieldInfo")
    Flowable<BaseDto<LandDto>> getLand(@Path("organizationId") String organizationId,
                                       @Query("userName") String userName,
                                       @Query("fieldName") String fieldName);


    //查询某设备一段时间的照片数据
    @GET("/asset/v1/{organizationId}/quality-analysis/DeviceImages")
    Flowable<BaseDto<List<SinglePicture>>> getPictureDataBySn(@Path("organizationId") String organizationId,
                                                              @Query("sns") String sns,
                                                              @Query("startDate") String startDate,
                                                              @Query("endDate") String endDate);
}
