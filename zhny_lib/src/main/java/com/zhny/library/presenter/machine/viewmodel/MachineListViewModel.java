package com.zhny.library.presenter.machine.viewmodel;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.machine.model.dto.MachineDto;
import com.zhny.library.presenter.machine.repository.IMachineRepository;
import com.zhny.library.presenter.machine.repository.impl.MachineRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * 登录页面viewmodel
 */
public class MachineListViewModel extends ViewModel {
    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }


    //请求machine list 数据
    public LiveData<BaseDto<List<MachineDto>>> getMachineList() {
        IMachineRepository machineRepository = new MachineRepository(null, context);
        return machineRepository.getMachines(
                SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, ""),
                SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, "")
        );
    }
}
