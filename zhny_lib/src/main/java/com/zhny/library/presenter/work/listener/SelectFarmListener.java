package com.zhny.library.presenter.work.listener;

import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;

/**
 * created by liming
 */
public interface SelectFarmListener {

    /**
     * select farm for show or hide plot list
     *
     * @param selectFarmDto farm dto
     */
    void onFarmSelected(SelectFarmDto selectFarmDto);

}
