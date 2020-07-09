package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.driver.model.dto.DriverDto;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 我的机手api
 */
public interface DriverApiService {


    /**
     * 查询机手列表
     *
     * @param organizationId 组织id
     * @return 机手列表
     */
    @GET("/asset/v1/{organizationId}/worker/workerList")
    Flowable<BaseDto<List<DriverDto>>> getDrivers(@Path("organizationId") String organizationId);


    /**
     * 添加/删除/编辑机手信息
     *
     * @param organizationId 组织id
     */
    @POST("/asset/v1/{organizationId}/worker/save")
    Flowable<BaseDto<Boolean>> saveDriver(@Path("organizationId") String organizationId,
                                          @Body RequestBody requestBody);


}
