package com.zhny.library.presenter.work.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WorkTrackDto implements Serializable {


    /**
     * deviceSn : SN_7190904695
     * width : 376.55
     * trackList : [{"speed":null,"latitude":37.47451,"longitude":114.96227,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:49","workingState":null},{"speed":null,"latitude":37.47455,"longitude":114.96227,"depth":18.3,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:51","workingState":null},{"speed":null,"latitude":37.47458,"longitude":114.96227,"depth":27,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:53","workingState":null},{"speed":null,"latitude":37.47462,"longitude":114.96227,"depth":30,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:55","workingState":null},{"speed":null,"latitude":37.474663,"longitude":114.962265,"depth":31.4,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:57","workingState":null},{"speed":null,"latitude":37.47471,"longitude":114.96226,"depth":31.1,"jobTypeDetail":null,"trackDate":"2020-02-20 13:18:59","workingState":null},{"speed":null,"latitude":37.474754,"longitude":114.96226,"depth":30.9,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:01","workingState":null},{"speed":null,"latitude":37.4748,"longitude":114.96225,"depth":30.4,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:03","workingState":null},{"speed":null,"latitude":37.474846,"longitude":114.96225,"depth":30,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:05","workingState":null},{"speed":null,"latitude":37.47489,"longitude":114.96224,"depth":29.9,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:07","workingState":null},{"speed":null,"latitude":37.474915,"longitude":114.96225,"depth":29.3,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:09","workingState":null},{"speed":null,"latitude":37.47493,"longitude":114.96224,"depth":28.9,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:11","workingState":null},{"speed":null,"latitude":37.474945,"longitude":114.96224,"depth":28.8,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:13","workingState":null},{"speed":null,"latitude":37.47496,"longitude":114.962234,"depth":28.1,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:15","workingState":null},{"speed":null,"latitude":37.47498,"longitude":114.962234,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:17","workingState":null},{"speed":null,"latitude":37.475,"longitude":114.96223,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:19","workingState":null},{"speed":null,"latitude":37.475014,"longitude":114.96222,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:21","workingState":null},{"speed":null,"latitude":37.47503,"longitude":114.96221,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:23","workingState":null},{"speed":null,"latitude":37.475048,"longitude":114.962204,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:25","workingState":null},{"speed":null,"latitude":37.47506,"longitude":114.9622,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:27","workingState":null},{"speed":null,"latitude":37.475048,"longitude":114.9622,"depth":0,"jobTypeDetail":null,"trackDate":"2020-02-20 13:19:29","workingState":null}]
     */

    @SerializedName("deviceSn")
    public String deviceSn;
    @SerializedName("width")
    public double width;
    @SerializedName("trackList")
    public List<TrackListBean> trackList;

    public static class TrackListBean {
        /**
         * speed : null
         * latitude : 37.47451
         * longitude : 114.96227
         * depth : 0
         * jobTypeDetail : null
         * trackDate : 2020-02-20 13:18:49
         * workingState : null
         */

//        @SerializedName("speed")
//        public Object speed;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("depth")
        public double depth;
//        @SerializedName("jobTypeDetail")
//        public String jobTypeDetail;
//        @SerializedName("trackDate")
//        public String trackDate;
//        @SerializedName("workingState")
//        public String workingState;


        public TrackListBean(double latitude, double longitude, double depth) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.depth = depth;
        }
    }
}