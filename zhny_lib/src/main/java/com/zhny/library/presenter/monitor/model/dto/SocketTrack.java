package com.zhny.library.presenter.monitor.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class SocketTrack implements Serializable {
    @SerializedName("ifaceId")
    public String ifaceId;
    @SerializedName("reported")
    public ReportedBean reported;

    @Override
    public String toString() {
        return "SocketTrack{" +
                "ifaceId='" + ifaceId + '\'' +
                ", reported=" + reported +
                '}';
    }

    public static class ReportedBean implements Serializable {
        @SerializedName("width")
        public String width;
        @SerializedName("startPoint")
        public StartPointBean startPoint;
        @SerializedName("endPoint")
        public EndPointBean endPoint;
        @SerializedName("posList")
        public List<PosListBean> posList;

        @Override
        public String toString() {
            return "ReportedBean{" +
                    "width='" + width + '\'' +
                    ", startPoint=" + startPoint +
                    ", endPoint=" + endPoint +
                    ", posList=" + posList +
                    '}';
        }

        public static class StartPointBean implements Serializable {
            @SerializedName("latitude")
            public String latitude;
            @SerializedName("longitude")
            public String longitude;

            @Override
            public String toString() {
                return "StartPointBean{" +
                        "latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        '}';
            }
        }

        public static class EndPointBean implements Serializable {
            @SerializedName("latitude")
            public String latitude;
            @SerializedName("longitude")
            public String longitude;

            @Override
            public String toString() {
                return "EndPointBean{" +
                        "latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        '}';
            }
        }

        public static class PosListBean implements Serializable {
            @SerializedName("latitude")
            public String latitude;
            @SerializedName("longitude")
            public String longitude;
            @SerializedName("speed")
            public String speed;

            @Override
            public String toString() {
                return "PosListBean{" +
                        "latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        ", speed='" + speed + '\'' +
                        '}';
            }
        }
    }
}
