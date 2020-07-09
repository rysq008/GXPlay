package com.zhny.library.presenter.monitor.model.dto;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polygon;
import com.google.gson.annotations.SerializedName;
import com.zhny.library.presenter.monitor.model.vo.MapPath;

import java.io.Serializable;
import java.util.List;

/**
 * farm json
 *
 * created by liming
 */
public class SelectFarmDto implements Serializable {


    /**
     * creationDate : null
     * createdBy : null
     * lastUpdateDate : null
     * lastUpdatedBy : null
     * objectVersionNumber : null
     * farmId : 1
     * farmCode : FARM01
     * farmName : 农场1
     * userName : USER1
     * isDel : 0
     * organizationId : -1
     * tenantId : -1
     * fieldList : [{"creationDate":1581082169000,"createdBy":-1,"lastUpdateDate":1581082169000,"lastUpdatedBy":-1,"objectVersionNumber":1,"fieldId":1,"fieldCode":"FIELD01","fieldName":"地块1","farmCode":"FARM01","coordinates":"[[[[111.14803936544962,41.527694],[111.15593130363298,41.528579],[111.06180313182605,41.528339],[111.11868116893478,41.526878],[111.12629931700306,41.525215],[110.99692918981881,41.522613],[111.0438767589849,41.522418],[110.98934804792427,41.521514],[110.99882690555225,41.527694]]]]",
     * "fieldArea":"360314.82537961006","center":"111.0710801099477,41.52567808842154","gravity":"111.03798959521052,41.52503614237198","province":"内蒙古自治区","city":"包头市","district":"达尔罕茂明安联合旗","type":"MultiPolygon","isDel":0,"organizationId":-1,"tenantId":-1}]
     */


    @SerializedName("farmId")
    public int farmId;

    @SerializedName("farmCode")
    public String farmCode;

    @SerializedName("farmName")
    public String farmName;

    @SerializedName("userName")
    public String userName;

    @SerializedName("fieldList")
    public List<SelectPlotDto> fieldList;

    public SelectFarmDto() {
    }

    public SelectFarmDto(String farmName, List<SelectPlotDto> fieldList) {
        this.farmName = farmName;
        this.fieldList = fieldList;
    }

    @Override
    public String toString() {
        return "SelectFarmDto{" +
                "farmId=" + farmId +
                ", farmCode='" + farmCode + '\'' +
                ", farmName='" + farmName + '\'' +
                ", userName='" + userName + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }

    public static class SelectPlotDto implements Serializable {

        public MapPath mapPath;

        public List<LatLng> latLngs;

        public Marker plotMarker;

        public Polygon polygon;

        @SerializedName("fieldId")
        public long fieldId;

        @SerializedName("fieldCode")
        public String fieldCode;

        @SerializedName("fieldName")
        public String fieldName;

        @SerializedName("farmCode")
        public String farmCode;

        @SerializedName("coordinates")
        public String coordinates;

        @SerializedName("fieldArea")
        public String fieldArea;

        @SerializedName("center")
        public String center;

        @SerializedName("gravity")
        public String gravity;

        @SerializedName("province")
        public String province;

        @SerializedName("city")
        public String city;

        @SerializedName("district")
        public String district;

        @SerializedName("type")
        public String type;

        @Override
        public String toString() {
            return "SelectPlotDto{" +
                    "fieldId=" + fieldId +
                    ", fieldCode='" + fieldCode + '\'' +
                    ", fieldName='" + fieldName + '\'' +
                    ", farmCode='" + farmCode + '\'' +
                    ", coordinates='" + coordinates + '\'' +
                    ", fieldArea='" + fieldArea + '\'' +
                    ", center='" + center + '\'' +
                    ", gravity='" + gravity + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }



}
