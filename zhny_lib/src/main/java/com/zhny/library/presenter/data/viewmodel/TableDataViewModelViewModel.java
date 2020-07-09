package com.zhny.library.presenter.data.viewmodel;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.data.model.dto.DataDeviceDto;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.repository.IDataRepository;
import com.zhny.library.presenter.data.repository.impl.DataRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TableDataViewModelViewModel extends ViewModel {
    private Context context;

    public MutableLiveData<Boolean> hasData = new MutableLiveData<>();

    public void setParams(Context context) {
        this.context = context;
    }


    public void setHasData(boolean result) {
        hasData.setValue(result);
    }

    //请求报表数据
    public LiveData<BaseDto<JobReportDto>> getJobReport(String startDate, String endDate, String snList, int sortRule){
        IDataRepository dataRepository = new DataRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, "1");
        boolean deviceFlag = !TextUtils.isEmpty(snList);
        return dataRepository.getJobReport(organizationId, userId, startDate, endDate, snList, deviceFlag, sortRule);
    }

    //请求设备
    public LiveData<BaseDto<List<DataDeviceDto>>> getDataDevices(){
        IDataRepository dataRepository = new DataRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, "1");
        return dataRepository.getDataDevices(organizationId, userId);
    }

}
