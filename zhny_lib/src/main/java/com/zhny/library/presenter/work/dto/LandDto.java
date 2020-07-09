package com.zhny.library.presenter.work.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LandDto implements Serializable {

    /**
     * totalPages : 1
     * totalElements : 1
     * numberOfElements : 1
     * size : 10
     * number : 0
     * content : [{"creationDate":null,"createdBy":null,"lastUpdateDate":null,"lastUpdatedBy":null,"objectVersionNumber":null,"fieldId":1,"fieldCode":"FIELD01","fieldName":"地块1","farmCode":"FARM01","coordinates":"[[[[111.14803936544962,41.527694],[111.15593130363298,41.528579],[111.06180313182605,41.528339],[111.11868116893478,41.526878],[111.12629931700306,41.525215],[110.99692918981881,41.522613],[111.0438767589849,41.522418],[110.98934804792427,41.521514],[110.99882690555225,41.527694]]]]","fieldArea":"360314.82537961006","center":"111.0710801099477,41.52567808842154","gravity":"111.03798959521052,41.52503614237198","province":"内蒙古自治区","city":"包头市","district":"达尔罕茂明安联合旗","type":"MultiPolygon","isDel":0,"organizationId":-1,"tenantId":-1,"farm":{"creationDate":1581082107000,"createdBy":-1,"lastUpdateDate":1581082107000,"lastUpdatedBy":null,"objectVersionNumber":1,"farmId":1,"farmCode":"FARM01","farmName":"农场1","userName":"admin","longitude":111,"latitud":42,"isDel":0,"organizationId":-1,"tenantId":-1,"fieldList":null}}]
     * empty : false
     */

    @SerializedName("totalPages")
    public int totalPages;
    @SerializedName("totalElements")
    public int totalElements;
    @SerializedName("numberOfElements")
    public int numberOfElements;
    @SerializedName("size")
    public int size;
    @SerializedName("number")
    public int number;
    @SerializedName("empty")
    public boolean empty;
    @SerializedName("content")
    public List<ContentBean> content;

    public static class ContentBean {
        /**
         * creationDate : null
         * createdBy : null
         * lastUpdateDate : null
         * lastUpdatedBy : null
         * objectVersionNumber : null
         * fieldId : 1
         * fieldCode : FIELD01
         * fieldName : 地块1
         * farmCode : FARM01
         * coordinates : [[[[111.14803936544962,41.527694],[111.15593130363298,41.528579],[111.06180313182605,41.528339],[111.11868116893478,41.526878],[111.12629931700306,41.525215],[110.99692918981881,41.522613],[111.0438767589849,41.522418],[110.98934804792427,41.521514],[110.99882690555225,41.527694]]]]
         * fieldArea : 360314.82537961006
         * center : 111.0710801099477,41.52567808842154
         * gravity : 111.03798959521052,41.52503614237198
         * province : 内蒙古自治区
         * city : 包头市
         * district : 达尔罕茂明安联合旗
         * type : MultiPolygon
         * isDel : 0
         * organizationId : -1
         * tenantId : -1
         * farm : {"creationDate":1581082107000,"createdBy":-1,"lastUpdateDate":1581082107000,"lastUpdatedBy":null,"objectVersionNumber":1,"farmId":1,"farmCode":"FARM01","farmName":"农场1","userName":"admin","longitude":111,"latitud":42,"isDel":0,"organizationId":-1,"tenantId":-1,"fieldList":null}
         */

        @SerializedName("creationDate")
        public Object creationDate;
        @SerializedName("createdBy")
        public Object createdBy;
        @SerializedName("lastUpdateDate")
        public Object lastUpdateDate;
        @SerializedName("lastUpdatedBy")
        public Object lastUpdatedBy;
        @SerializedName("objectVersionNumber")
        public Object objectVersionNumber;
        @SerializedName("fieldId")
        public int fieldId;
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
        @SerializedName("isDel")
        public int isDel;
        @SerializedName("organizationId")
        public int organizationId;
        @SerializedName("tenantId")
        public int tenantId;
        @SerializedName("farm")
        public FarmBean farm;

        public static class FarmBean {
            /**
             * creationDate : 1581082107000
             * createdBy : -1
             * lastUpdateDate : 1581082107000
             * lastUpdatedBy : null
             * objectVersionNumber : 1
             * farmId : 1
             * farmCode : FARM01
             * farmName : 农场1
             * userName : admin
             * longitude : 111
             * latitud : 42
             * isDel : 0
             * organizationId : -1
             * tenantId : -1
             * fieldList : null
             */

            @SerializedName("creationDate")
            public long creationDate;
            @SerializedName("createdBy")
            public int createdBy;
            @SerializedName("lastUpdateDate")
            public long lastUpdateDate;
            @SerializedName("lastUpdatedBy")
            public Object lastUpdatedBy;
            @SerializedName("objectVersionNumber")
            public int objectVersionNumber;
            @SerializedName("farmId")
            public int farmId;
            @SerializedName("farmCode")
            public String farmCode;
            @SerializedName("farmName")
            public String farmName;
            @SerializedName("userName")
            public String userName;
            @SerializedName("longitude")
            public int longitude;
            @SerializedName("latitud")
            public int latitud;
            @SerializedName("isDel")
            public int isDel;
            @SerializedName("organizationId")
            public int organizationId;
            @SerializedName("tenantId")
            public int tenantId;
            @SerializedName("fieldList")
            public Object fieldList;
        }
    }
}
