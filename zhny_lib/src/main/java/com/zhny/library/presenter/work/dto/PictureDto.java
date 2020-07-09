package com.zhny.library.presenter.work.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PictureDto implements Serializable {


    /**
     * jobId : null
     * sn : test1111
     * code : test
     * jobType : 1
     * jobTypeMeaning : 深翻作业
     * startTime : 2020-02-07 00:00:00
     * endTime : 2020-02-07 23:55:55
     * jobDuration : 0
     * imgs : [{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.706497,"longitude":119.26529},{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.71006,"longitude":119.264885},{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.71015,"longitude":119.26488},{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.71029,"longitude":119.26486},{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.710327,"longitude":119.26486},{"url":"http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg","latitude":39.71041,"longitude":119.26484}]
     */


    @SerializedName("sn")
    public String sn;
    @SerializedName("code")
    public String code;
    @SerializedName("jobType")
    public int jobType;
    @SerializedName("jobTypeMeaning")
    public String jobTypeMeaning;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("jobDuration")
    public int jobDuration;
    @SerializedName("imgs")
    public List<ImgBean> imgs;

    public static class ImgBean {
        /**
         * url : http://101.200.32.194:89/photos/20191016/7170926333_2019101602955.jpeg
         * latitude : 39.706497
         * longitude : 119.26529
         */

        @SerializedName("url")
        public String url;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;

        @Override
        public String toString() {
            return "ImgBean{" +
                    "url='" + url + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }

    }
}
