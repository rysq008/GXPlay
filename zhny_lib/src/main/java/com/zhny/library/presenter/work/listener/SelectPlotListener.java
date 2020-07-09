package com.zhny.library.presenter.work.listener;

import com.zhny.library.presenter.monitor.model.dto.SelectFarmDto;

/**
 * created by liming
 */
public interface SelectPlotListener {

    /**
     * select plot form farm
     *
     * @param selectPlotDto plot dto
     */
    void onPlotSelected(SelectFarmDto.SelectPlotDto selectPlotDto);

}
