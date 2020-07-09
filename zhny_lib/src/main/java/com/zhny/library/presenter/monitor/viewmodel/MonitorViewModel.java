package com.zhny.library.presenter.monitor.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.base.BaseActivity;
import com.zhny.library.base.BaseDto;
import com.zhny.library.common.Constant;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.login.repository.ILoginRepository;
import com.zhny.library.presenter.login.repository.impl.LoginRepository;
import com.zhny.library.presenter.monitor.model.dto.LookUpVo;
import com.zhny.library.presenter.monitor.model.dto.MachineProper;
import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;
import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;
import com.zhny.library.presenter.monitor.repository.IMonitoryRepository;
import com.zhny.library.presenter.monitor.repository.impl.MonitoryRepository;
import com.zhny.library.presenter.monitor.view.MonitorActivity;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorViewModel extends ViewModel {
    private static final String TAG = "MonitorViewModel";

    private Context context;

    public void setParams(Context context) {
        this.context = context;
    }

    //农机选择列表是否显示农机列表
    public MutableLiveData<Boolean> isShowMachines = new MutableLiveData<>();
    //是否显示定位按钮
    public MutableLiveData<Boolean> isShowLocationView = new MutableLiveData<>();
    //是否显示农机数据
    public MutableLiveData<Boolean> isShowBaseDataView = new MutableLiveData<>();
    //农机数据
    public MutableLiveData<SelectMachineDto> machineDto = new MutableLiveData<>();
    //设备在线数量
    public MutableLiveData<Integer> deviceOnline = new MutableLiveData<>();
    public MutableLiveData<Integer> deviceOffline = new MutableLiveData<>();
    //设备实时数据
    public MutableLiveData<MachineProper> machineProper = new MutableLiveData<>();
    //是否可以点击实时监控按钮
    public MutableLiveData<Boolean> canMonitoring = new MutableLiveData<>();


    public void setShowOrHideBaseData(boolean isShow) {
        isShowBaseDataView.setValue(isShow);
    }

    public void setShowOrHideMachine(boolean isShow) {
        isShowMachines.setValue(isShow);
    }

    public void setShowOrHideLocationView(boolean isShow) {
        isShowLocationView.setValue(isShow);
    }

    public void setBaseData(SelectMachineDto machine) {
        machineDto.setValue(machine);
    }

    public void setMonitoringButton(boolean status) {
        canMonitoring.setValue(status);
    }

    public void setDevicesNum(int online, int offline) {
        deviceOnline.setValue(online);
        deviceOffline.setValue(offline);
    }


    //请求 地块 数据
    public LiveData<BaseDto<List<SelectFarmDto>>> getPlotData() {
        IMonitoryRepository monitoryRepository = new MonitoryRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userName = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USERNAME, "admin");
        return monitoryRepository.getPlotData(organizationId, userName);
    }

    //请求设备信息
    public LiveData<BaseDto<List<SelectMachineDto>>> getDevices() {
        IMonitoryRepository monitoryRepository = new MonitoryRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        String userId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.USER_ID, "-1");
        return monitoryRepository.getDevices(organizationId, userId);
    }

    //请求设备实时信息
    public void getDevicesPropers(BaseActivity activity, String deviceSn, String date) {
        IMonitoryRepository monitoryRepository = new MonitoryRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");
        monitoryRepository.getDevicesPropers(organizationId, deviceSn, date).observe(activity, baseDto -> {
            if (baseDto.getContent() != null) {
                List<MachineProper> propers = baseDto.getContent();
                if (propers != null && propers.size() != 0) {
                    machineProper.setValue(propers.get(0));
                }
            }
        });
    }


    public LiveData<TokenInfoDto> getToken(Map<String, String> params){
        ILoginRepository loginRepository = new LoginRepository();
        return loginRepository.getToken(context, null, params);
    }

    public LiveData<UserInfoDto> getUserInfo(Context context){
        ILoginRepository loginRepository = new LoginRepository(null, context);
        return loginRepository.getUserInfo();
    }


    public void queryFastCode(MonitorActivity activity) {
        ILoginRepository loginRepository = new LoginRepository(null, context);
        String organizationId = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.ORGANIZATION_ID, "1");

        //@1 : 请求快码 ->  轨迹离线超时时间
        loginRepository.queryFastCode(organizationId, Constant.FINALVALUE.OFF_LINE_SECOND_CODE).observe(activity, baseDto -> {
            if (baseDto != null && baseDto.getContent() != null && baseDto.getContent().values != null && baseDto.getContent().values.size() > 0) {
                LookUpVo.ValuesBean valuesBean = baseDto.getContent().values.get(0);
                if (!TextUtils.isEmpty(valuesBean.description)) {
                    int offLineSecond = Integer.valueOf(valuesBean.description);
                    Log.d(TAG, "queryFastCode: 存入快码 ： OFF_LINE_SECOND_CODE ==>" + offLineSecond);
                    SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.OFF_LINE_SECOND_CODE, offLineSecond);
                }
            }
        });

        //@2 : 请求快码 ->  显示作业质量深度的作业类型
        loginRepository.queryFastCode(organizationId, Constant.FINALVALUE.DEEP_OF_WORK_TYPE_CODE).observe(activity, baseDto -> {
            if (baseDto != null && baseDto.getContent() != null && baseDto.getContent().values != null) {
                String code = "";
                for (LookUpVo.ValuesBean value : baseDto.getContent().values) {
                    code = code.concat(value.code).concat(",");
                }
                if (!TextUtils.isEmpty(code) && code.length() > 1) {
                    code = code.substring(0, code.length() - 1);
                    Log.d(TAG, "queryFastCode: 存入快码 ： DEEP_OF_WORK_TYPE_CODE ==>" + code);
                    SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.FINALVALUE.DEEP_OF_WORK_TYPE_CODE, code);
                }
            }
        });
    }
}
