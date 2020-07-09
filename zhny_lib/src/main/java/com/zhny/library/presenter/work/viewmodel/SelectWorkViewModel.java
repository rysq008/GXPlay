package com.zhny.library.presenter.work.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.work.dto.WorkDto;
import com.zhny.library.presenter.work.repository.IWorkRepository;
import com.zhny.library.presenter.work.repository.impl.WorkRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectWorkViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<String> landName = new MutableLiveData<>();

    public MutableLiveData<Boolean> showFristWorkList = new MutableLiveData<>();

    public MutableLiveData<Boolean> showSecWorkList = new MutableLiveData<>();

    //请求所有年份work 数据
    public LiveData<BaseDto<List<WorkDto>>> getWorkData(String fieldCode) {
        IWorkRepository workRepository = new WorkRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID);
        return workRepository.getWorks(organizationId, fieldCode);
    }


    //请求某个年份 work 数据
    public LiveData<BaseDto<List<WorkDto>>> getOneYearWorkData(String year) {
        IWorkRepository workRepository = new WorkRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID);
        return workRepository.getOneYearWorks(organizationId, "11944", year);
    }


}
