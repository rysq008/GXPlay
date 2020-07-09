package com.zhny.library.presenter.machine.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.repository.IMachineRepository;
import com.zhny.library.presenter.machine.repository.impl.MachineRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 设备详情页面viewmodel
 */
public class MachineDetailsViewmodel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    public MutableLiveData<String> jobTypeMeaning = new MutableLiveData<>();

    public MutableLiveData<String> horsepower = new MutableLiveData<>(); //马力

    public MutableLiveData<String> produceDate = new MutableLiveData<>(); //出厂日期

    public MutableLiveData<String> produceNumber = new MutableLiveData<>(); //出厂编号

    public MutableLiveData<String> carNumber = new MutableLiveData<>(); //车牌号

    public MutableLiveData<String> width = new MutableLiveData<>(); //幅宽

    public MutableLiveData<String> monitorTerminalID = new MutableLiveData<>(); //监控终端ID

    public MutableLiveData<String> creationDate = new MutableLiveData<>(); //添加时间

    //请求machine详情数据
    public LiveData<BaseDto<MachineDetailsDto>> getMachineDetails(String deviceSn) {
        IMachineRepository machineRepository = new MachineRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "");
        return machineRepository.getMachineDetails(organizationId, deviceSn);
    }


}
