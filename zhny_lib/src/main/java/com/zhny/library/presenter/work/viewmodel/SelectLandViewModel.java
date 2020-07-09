package com.zhny.library.presenter.work.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.work.repository.IWorkRepository;
import com.zhny.library.presenter.work.repository.impl.WorkRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectLandViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }


    public MutableLiveData<Boolean> isShowClear = new MutableLiveData<>();

    public void setIsShowClear(boolean showClear) {
        this.isShowClear.setValue(showClear);
    }

    //请求farm 数据
    public LiveData<BaseDto<List<SelectFarmDto>>> getFarmData() {
        IWorkRepository workRepository = new WorkRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        String userName = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USERNAME , "");
        return workRepository.getFarms(organizationId, userName);
    }


}
