package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.alarm.dto.AlarmDto;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 预警api
 */
public interface AlarmApiService {

    /**
     * 查询报警消息列表
     *
     * @param organizationId 组织id
     * @return 报警消息列表
     */
    @GET("/asset/v1/{organizationId}/alarm/list")
    Flowable<BaseDto<List<AlarmDto>>> getAlarms(@Path("organizationId") String organizationId,
                                                @Query("isDel") Boolean isDel,
                                                @Query("userId") String userId);

    /**
     * 批量阅读
     */
    @POST("/asset/v1/{organizationId}/alarm/batchRead")
    Flowable<BaseDto<Boolean>> batchRead(@Path("organizationId") String organizationId,
                                         @Body List<Integer> msgIds);


}
