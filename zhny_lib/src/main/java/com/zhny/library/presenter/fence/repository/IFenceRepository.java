package com.zhny.library.presenter.fence.repository;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.dto.PageInfo;
import com.zhny.library.presenter.fence.model.vo.FenceVo;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * created by liming
 */
public interface IFenceRepository {

    LiveData<BaseDto<PageInfo<List<Fence>>>> getFences(String organizationId);

    LiveData<BaseDto<List<FenceMachine>>> getFenceCanAddDevices(String organizationId, Integer fenceId, String points);

    LiveData<BaseDto<Fence>> getFenceDetails(String organizationId, int fenceId);

    LiveData<BaseDto<Fence>> addFence(String organizationId, FenceVo fence);

    LiveData<BaseDto<Boolean>> updateFence(String organizationId, FenceVo fence);

    LiveData<BaseDto<Boolean>> disableFence(String organizationId, int fenceId, Boolean disable);
}
