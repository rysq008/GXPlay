package com.zhny.library.presenter.fence.repository.impl;

import android.content.Context;

import com.zhny.library.base.BaseDto;
import com.zhny.library.base.BaseRepository;
import com.zhny.library.https.retrofit.RequestRetrofit;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.dto.PageInfo;
import com.zhny.library.presenter.fence.model.vo.FenceVo;
import com.zhny.library.https.api.FenceApiService;
import com.zhny.library.presenter.fence.repository.IFenceRepository;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;

/**
 * created by liming
 */
public class FenceRepository extends BaseRepository implements IFenceRepository {

    private Context context;
    private LoadingDialog dialog;

    public FenceRepository(LoadingDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }


    @Override
    public LiveData<BaseDto<PageInfo<List<Fence>>>> getFences(String organizationId) {
        Flowable<BaseDto<PageInfo<List<Fence>>>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .getFences(organizationId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<List<FenceMachine>>> getFenceCanAddDevices(String organizationId, Integer fenceId, String points) {
        Flowable<BaseDto<List<FenceMachine>>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .getFenceCanAddDevices(organizationId, fenceId, points);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Fence>> getFenceDetails(String organizationId, int fenceId) {
        Flowable<BaseDto<Fence>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .getFenceDetail(organizationId, fenceId);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Fence>> addFence(String organizationId, FenceVo fence) {
        Flowable<BaseDto<Fence>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .addFence(organizationId, fence);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Boolean>> updateFence(String organizationId, FenceVo fence) {
        Flowable<BaseDto<Boolean>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .updateFence(organizationId, fence);
        return request(flowable).get();
    }

    @Override
    public LiveData<BaseDto<Boolean>> disableFence(String organizationId, int fenceId, Boolean disable) {
        Flowable<BaseDto<Boolean>> flowable = RequestRetrofit
                .getInstance(context, dialog, FenceApiService.class)
                .disableFence(organizationId, fenceId, disable);
        return request(flowable).get();
    }
}
