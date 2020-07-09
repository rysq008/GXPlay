package com.zhny.library.presenter.monitor.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class ExpandData implements Serializable {

    public int dataType; // 1农机 2地块
    public String name;
    public int size;
    public List<Object> content;

    public ExpandData() {
    }

    public ExpandData(int dataType, String name) {
        this.dataType = dataType;
        this.name = name;
    }

    public ExpandData(int dataType, String name, int size, List<Object> content) {
        this.dataType = dataType;
        this.name = name;
        this.size = size;
        this.content = content;
    }
}
