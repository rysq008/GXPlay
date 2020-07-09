package com.zhny.library.presenter.monitor.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class PlotDeviceDto implements Serializable {

    public String name;
    public String key;
    public int dataType;

    public List<Object> data;

}
