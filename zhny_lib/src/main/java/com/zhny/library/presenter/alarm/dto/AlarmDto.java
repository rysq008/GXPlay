package com.zhny.library.presenter.alarm.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AlarmDto implements Serializable {


    /**
     * startDate转换后的时间
     * 当日显示：上午 9:00         下午 2:12
     * 昨日显示：昨日上午 9:00     昨日下午 2:12
     * 更早显示：日期+上下午+时间   2019年6月30日上午 9:00
     */
    public String convertDate;
    public int msgType = -1;


    /**
     * creationDate : 1587692379000
     * createdBy : 1
     * lastUpdateDate : 1587692379000
     * lastUpdatedBy : 1
     * objectVersionNumber : 1
     * id : 256
     * alarmNum : AE_000071
     * deviceSn : SN_7190411453
     * geofenceId : -1
     * typeCode : JOB_INFO
     * description : 地块223438有新的作业信息
     * isDealFlag : false
     * isEndFlag : false
     * isDel : false
     * fieldCode : 223438
     * jobType : 5
     * jobTypeMeaning : 播种作业
     * startDate : 2020-04-01 11:00:00
     * endDate : 2020-04-01 12:00:00
     * organizationId : -1
     * tenantId : -1
     * typeMeaning : 作业信息
     * deviceName : 信翔453
     * geofenceName : null
     * userId : null
     * deal : null
     * isRead : true
     * userName : null
     * farmName : T03农场
     * fieldName : T03用户地块APP渲染
     * fieldCenter : 111.0710801099477,41.52567808842154
     * fieldCoordinates : [[[[80.380012,40.426827],[80.380066,40.422351],[80.384561,40.422473],[80.384325,40.427006],[80.380012,40.426827]]]]
     */

    @SerializedName("id")
    public int id;
    @SerializedName("alarmNum")
    public String alarmNum;
    @SerializedName("deviceSn")
    public String deviceSn;
    @SerializedName("geofenceId")
    public int geofenceId;
    @SerializedName("typeCode")
    public String typeCode;
    @SerializedName("description")
    public String description;
    @SerializedName("isDealFlag")
    public boolean isDealFlag;
    @SerializedName("isEndFlag")
    public boolean isEndFlag;
    @SerializedName("isDel")
    public boolean isDel;
    @SerializedName("fieldCode")
    public String fieldCode;
    @SerializedName("jobType")
    public int jobType;
    @SerializedName("jobTypeMeaning")
    public String jobTypeMeaning;
    @SerializedName("startDate")
    public String startDate;
    @SerializedName("endDate")
    public String endDate;
    @SerializedName("organizationId")
    public int organizationId;
    @SerializedName("tenantId")
    public int tenantId;
    @SerializedName("typeMeaning")
    public String typeMeaning;
    @SerializedName("deviceName")
    public String deviceName;
    @SerializedName("geofenceName")
    public String geofenceName;
    @SerializedName("isRead")
    public boolean isRead;
    @SerializedName("userName")
    public String userName;
    @SerializedName("farmName")
    public String farmName;
    @SerializedName("fieldName")
    public String fieldName;
    @SerializedName("fieldCenter")
    public String fieldCenter;
    @SerializedName("fieldCoordinates")
    public String fieldCoordinates;


}
