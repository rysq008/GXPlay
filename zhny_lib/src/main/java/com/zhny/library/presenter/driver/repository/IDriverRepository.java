package com.zhny.library.presenter.driver.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.driver.model.dto.DriverDto;

import java.util.List;

import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;


public interface IDriverRepository {

    LiveData<BaseDto<List<DriverDto>>> getDrivers(String organizationId);

    LiveData<BaseDto<Boolean>> saveDriver(String organizationId, RequestBody requestBody);
}
