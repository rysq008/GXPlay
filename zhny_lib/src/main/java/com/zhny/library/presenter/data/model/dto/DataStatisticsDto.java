package com.zhny.library.presenter.data.model.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class DataStatisticsDto implements Serializable {

    public String convertTotalArea;//转换后的总亩数，保留2位小数

    public String totalTimeHour;//总时长，时

    public String totalTimeMin;//总时长，分
    /**
     * todayArea : 0
     * todayTime : 00:00:00
     * thisMonthArea : 0
     * thisMonthTime : 00:00:00
     * totalArea : 1069.65
     * totalTime : 69:16:06
     * content : [{"yearArea":92.59,"yearTime":"07:39:33","year":"2020","detail":[{"month":"02","monthArea":92.59,"monthTime":"07:39:33"}]},{"yearArea":977.06,"yearTime":"61:36:33","year":"2019","detail":[{"month":"06","monthArea":977.06,"monthTime":"61:36:33"}]}]
     */

    @SerializedName("todayArea")
    public double todayArea;

    @SerializedName("todayTime")
    public String todayTime;

    @SerializedName("thisMonthArea")
    public double thisMonthArea;

    @SerializedName("thisMonthTime")
    public String thisMonthTime;

    @SerializedName("totalArea") //总亩数
    public double totalArea;

    @SerializedName("totalTime") //总时长
    public String totalTime;

    @SerializedName("content")
    public List<YearData> yearDataList;

    public static class YearData {
        /**
         * yearArea : 92.59
         * yearTime : 07:39:33
         * year : 2020
         * detail : [{"month":"02","monthArea":92.59,"monthTime":"07:39:33"}]
         */
        public String convertYearArea;//转换后的year亩数，保留2位小数

        public String yearTimeHour;//单年时长，时

        public String yearTimeMin;//单年时长，分


        @SerializedName("yearArea")
        public double yearArea;
        @SerializedName("yearTime")
        public String yearTime;
        @SerializedName("year")
        public String year;
        @SerializedName("detail")
        public List<MonthData> monthDataList;

        public static class MonthData {
            /**
             * month : 02
             * monthArea : 92.59
             * monthTime : 07:39:33
             */

            @SerializedName("month")
            public String month;
            @SerializedName("monthArea")
            public double monthArea;
            @SerializedName("monthTime")
            public String monthTime;
        }
    }
}
