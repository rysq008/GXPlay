package com.zhny.library.presenter.work.helper;

import java.io.Serializable;

public class YearInfo implements Serializable {

    public String year;
    public boolean isSelect;

    public YearInfo(String year, boolean isSelect) {
        this.year = year;
        this.isSelect = isSelect;
    }

    @Override
    public String toString() {
        return "YearInfo{" +
                "year='" + year + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}


