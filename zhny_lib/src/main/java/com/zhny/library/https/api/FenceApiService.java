package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.dto.PageInfo;
import com.zhny.library.presenter.fence.model.vo.FenceVo;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 电子围栏api
 */
public interface FenceApiService {

    /**
     * 请求地理围栏列表(分页)
     *
     * @param organizationId    organizationId
     * @return                  data
     */
    @GET("/asset/v1/{organizationId}/geofences?size=10000")
    Flowable<BaseDto<PageInfo<List<Fence>>>> getFences(@Path("organizationId") String organizationId);


    /**
     * 获取设备列表
     *
     * @param organizationId    organizationId
     * @param fenceId           fenceId
     * @return                  data
     */
    @GET("/asset/v1/{organizationId}/geofences/deviceList")
    Flowable<BaseDto<List<FenceMachine>>> getFenceCanAddDevices(@Path("organizationId") String organizationId,
                                                                @Query("geofenceId") Integer fenceId,
                                                                @Query("playPoints") String points);


    /**
     * 获取围栏详情
     *
     * @param organizationId    organizationId
     * @param fenceId           fenceId
     * @return                  data
     */
    @GET("/asset/v1/{organizationId}/geofences/{geofenceId}")
    Flowable<BaseDto<Fence>> getFenceDetail(@Path("organizationId") String organizationId,
                                            @Path("geofenceId") int fenceId);

    /**
     * 添加围栏
     *
     * @param organizationId    organizationId
     * @param fence             fence
     * @return                  data
     */
    @POST("/asset/v1/{organizationId}/geofences")
    Flowable<BaseDto<Fence>> addFence(@Path("organizationId") String organizationId,
                                      @Body FenceVo fence);

    /**
     * 编辑围栏
     *
     * @param organizationId    organizationId
     * @param fence             fence
     * @return                  data
     */
    @PUT("/asset/v1/{organizationId}/geofences")
    Flowable<BaseDto<Boolean>> updateFence(@Path("organizationId") String organizationId,
                                           @Body FenceVo fence);

    /**
     * 禁用围栏
     *
     * @param organizationId    organizationId
     * @param fenceId           fenceId
     * @param disabled          disabled
     * @return                  result
     */
    @PUT("/asset/v1/{organizationId}/geofences/{geofenceId}")
    Flowable<BaseDto<Boolean>> disableFence(@Path("organizationId") String organizationId,
                                            @Path("geofenceId") int fenceId,
                                            @Query("disabled") Boolean disabled);

}
