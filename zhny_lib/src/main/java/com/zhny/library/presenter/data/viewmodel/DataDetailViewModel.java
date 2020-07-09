package com.zhny.library.presenter.data.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.data.model.dto.JobReportDto;
import com.zhny.library.presenter.data.repository.IDataRepository;
import com.zhny.library.presenter.data.repository.impl.DataRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DataDetailViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    //请求报表数据
    public LiveData<BaseDto<JobReportDto>> getJobReport(String date){
        IDataRepository dataRepository = new DataRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, "1");
        return dataRepository.getJobReport(organizationId, userId, date, date, null, false, 1);
    }

    //获取一段时间内所有作业数据
    public LiveData<BaseDto<List<String>>> getWorkDate(String startDate, String endDate) {
        IDataRepository dataRepository = new DataRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        return dataRepository.getWorkDate(organizationId, startDate, endDate);
    }

}
