package com.zhny.library.presenter.data.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * table 数据分装
 *
 * created by liming
 */
public class CustomTableData implements Serializable {

    public List<String> topList; //第一行数据
    public List<Column> firstColumn; //第一列数据
    public List<List<Column>> dataColumn; //数据填充

    public CustomTableData() {
    }

    public CustomTableData(List<Column> firstColumn, List<List<Column>> dataColumn) {
        this.firstColumn = firstColumn;
        this.dataColumn = dataColumn;
    }



    public static class Column implements Serializable{
        public int index;
        public String value;

        public Column(int index, String value) {
            this.index = index;
            this.value = value;
        }
    }


}
