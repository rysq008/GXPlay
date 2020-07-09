package com.zhny.library.presenter.fence.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.model.dto.FenceMachine;
import com.zhny.library.presenter.fence.model.vo.FenceVo;
import com.zhny.library.presenter.fence.repository.IFenceRepository;
import com.zhny.library.presenter.fence.repository.impl.FenceRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FenceInfoViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    //农机选择列表是否显示农机列表
    public MutableLiveData<Fence> fenceData = new MutableLiveData<>();
    //是否是编辑围栏
    public MutableLiveData<Boolean> editFence = new MutableLiveData<>();

    public void setEditFence(boolean isEdit) {
        editFence.setValue(isEdit);
    }

    public MutableLiveData<Boolean> getEditFence() {
        return editFence;
    }

    public void setFenceData(Fence fence) {
        fenceData.setValue(fence);
    }


    //获取围栏对应的机具信息
    public LiveData<BaseDto<List<FenceMachine>>> getFenceCanAddDevices(Integer fenceId, String points) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.getFenceCanAddDevices(organizationId, fenceId, points);
    }

    //获取围栏详情
    public LiveData<BaseDto<Fence>> getFenceDetails(int fenceId) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.getFenceDetails(organizationId, fenceId);
    }

    //添加围栏
    public LiveData<BaseDto<Fence>> addFence(FenceVo fence) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.addFence(organizationId, fence);
    }

    //编辑围栏
    public LiveData<BaseDto<Boolean>> updateFence(FenceVo fence) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.updateFence(organizationId, fence);
    }

    //禁用围栏
    public LiveData<BaseDto<Boolean>> disableFence(int fenceId) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.disableFence(organizationId, fenceId, Boolean.TRUE);

    }

}
