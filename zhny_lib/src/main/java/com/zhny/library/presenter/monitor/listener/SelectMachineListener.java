package com.zhny.library.presenter.monitor.listener;

import com.zhny.library.presenter.monitor.model.dto.SelectMachineDto;

/**
 * created by liming
 */
public interface SelectMachineListener {

    /**
     * select machine
     *
     * @param machineDto    machine dto
     */
    void onMachineSelected(SelectMachineDto machineDto);

}
