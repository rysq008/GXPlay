package com.zhny.library.presenter.driver.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.driver.model.dto.DriverDto;
import com.zhny.library.presenter.driver.repository.IDriverRepository;
import com.zhny.library.presenter.driver.repository.impl.DriverRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

/**
 * 我的机手页面viewmodel
 */
public class DriverListViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> emptyDriver = new MutableLiveData<>(); //没有机手

    //请求我的机手列表 数据
    public LiveData<BaseDto<List<DriverDto>>> getDrivers() {
        IDriverRepository driverRepository = new DriverRepository(null, context);
        return driverRepository.getDrivers(SPUtils.getInstance(Constant.SP.SP_NAME)
                .getString(Constant.FINALVALUE.ORGANIZATION_ID, ""));
    }


    public LiveData<BaseDto<Boolean>> saveDriver(RequestBody requestBody) {
        IDriverRepository driverRepository = new DriverRepository(null, context);
        return driverRepository.saveDriver(SPUtils.getInstance(Constant.SP.SP_NAME)
                        .getString(Constant.FINALVALUE.ORGANIZATION_ID, ""),
                requestBody);
    }
}
