package com.zhny.library.presenter.machine.repository;


import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.machine.model.dto.MachineDetailsDto;
import com.zhny.library.presenter.machine.model.dto.MachineDto;

import java.util.List;

import androidx.lifecycle.LiveData;


public interface IMachineRepository {

    LiveData<BaseDto<List<MachineDto>>> getMachines(String organizationId, String userId);

    LiveData<BaseDto<MachineDetailsDto>> getMachineDetails(String organizationId, String deviceSn);
}
