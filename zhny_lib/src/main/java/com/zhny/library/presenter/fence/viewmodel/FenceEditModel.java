package com.zhny.library.presenter.fence.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.fence.model.dto.Fence;
import com.zhny.library.presenter.fence.repository.IFenceRepository;
import com.zhny.library.presenter.fence.repository.impl.FenceRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FenceEditModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    //获取围栏详情
    public LiveData<BaseDto<Fence>> getFenceDetails(int fenceId) {
        IFenceRepository fenceRepository = new FenceRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return fenceRepository.getFenceDetails(organizationId, fenceId);
    }

}
