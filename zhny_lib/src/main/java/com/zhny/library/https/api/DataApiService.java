package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.DataStatisticsDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 数据统计api
 */
public interface DataApiService {


    /**
     * 查询统计数据
     */
    @GET("/asset/v1/{organizationId}/data-statistics/job-info")
    Flowable<BaseDto<DataStatisticsDto>> getDataStatistics(@Path("organizationId") String organizationId);


    /**
     * 查询作业信息汇总报表
     *
     * @param deviceFlag        是否添加设备
     * @param sortRule          排序规则  1 作业时长  2作业面积   3 转运时长   4 离线时长
     * @return                  data
     */
    @GET("/asset/v1/{organizationId}/quality-analysis/jobReport")
    Flowable<BaseDto<JobReportDto>> getJobReport(@Path("organizationId") String organizationId,
                                                 @Query("userId") String userId,
                                                 @Query("startDate") String startDate,
                                                 @Query("endDate") String endDate,
                                                 @Query("snList") String snList,
                                                 @Query("deviceFlag") boolean deviceFlag,
                                                 @Query("sortRule") int sortRule);

    /**
     * 获取设备数据（不分页）
     */
    @GET("/asset/v1/{organizationId}/devices/list")
    Flowable<BaseDto<List<DataDeviceDto>>> getDataDevices(@Path("organizationId") String organizationId,
                                                          @Query("userId") String userId);


    /**
     * 获取一段时间有工作的日期组
     */
    @GET("/asset/v1/{organizationId}/devices/hasJobDate")
    Flowable<BaseDto<List<String>>> getWorkDate(@Path("organizationId") String organizationId,
                                                @Query("startDate") String startDate,
                                                @Query("endDate") String endDate);


}
