package com.zhny.library.presenter.driver.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.driver.repository.IDriverRepository;
import com.zhny.library.presenter.driver.repository.impl.DriverRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

/**
 * 添加机手页面viewmodel
 */
public class EditDriverViewmodel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<String> enterLimit = new MutableLiveData<>(); //没有机手
    public LiveData<BaseDto<Boolean>> saveDriver(RequestBody requestBody) {
        IDriverRepository driverRepository = new DriverRepository(null, context);
        return driverRepository.saveDriver(SPUtils.getInstance(Constant.SP.SP_NAME)
                        .getString(Constant.FINALVALUE.ORGANIZATION_ID, ""),
                requestBody);
    }

}
