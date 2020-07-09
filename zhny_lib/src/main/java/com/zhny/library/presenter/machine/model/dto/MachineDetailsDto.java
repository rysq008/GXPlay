package com.zhny.library.presenter.machine.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 机具列表返回实体
 */

public class MachineDetailsDto implements Serializable {


    /**
     * deviceId : 48
     * name : 信翔真实设备453
     * productType : 植保机-农机
     * productBrand : 14
     * productBrandMeaning : 泰达
     * productModel : 3WPZ-1200
     * imgUrl : https://zhny-dev-obs.obs.cn-north-4.myhuaweicloud.com/1582631490001.jpg,https://zhny-dev-obs.obs.cn-north-4.myhuaweicloud.com/1582631562687.jpg
     * outDate : null
     * sn : SN_7190411453
     * code :
     * lpn : null
     * propertyList : [{"name":"标定功率（kw）","value":"37"},{"name":"幅宽（cm）","value":"200.00"},{"name":"马力（ps）","value":"150"},{"name":"转速（r/min）","value":"2100"},{"name":"行驶速度（km/h）","value":"0"},{"name":"发动机功率（kw）","value":"155"},{"name":"方向","value":null},{"name":"海拔","value":null},{"name":"运行状态","value":"[{\"value\":\"0\",\"label\":\"正常\"},{\"value\":\"1\",\"label\":\"异常\"},{\"value\":\"2\",\"label\":\"故障\"}]"},{"name":"动力类型","value":"[{\"value\":\"1\",\"label\":\"燃油\"},{\"value\":\"0\",\"label\":\"柴油\"}]"},{"name":"生产厂家","value":"约翰迪尔公司"},{"name":"机械产地","value":"中国黑龙江省佳木斯"},{"name":"售后服务","value":"全国联保"},{"name":"适用领域","value":"[{\"value\":\"01\",\"label\":\"农业\"},{\"value\":\"02\",\"label\":\"林业\"},{\"value\":\"03\",\"label\":\"养殖业\"}]"},{"name":"适用场所","value":"[{\"value\":\"01\",\"label\":\"平地\"},{\"value\":\"02\",\"label\":\"山丘\"}]"},{"name":"生成类型","value":"进口"},{"name":"lcm","value":"lcm2"}]
     * jobType : 1
     * jobTypeMeaning : 深翻作业
     * tsn : null
     * province : 河北省
     * city : 邢台市
     * district : 宁晋县
     * latitude : 37.48993
     * longitude : 114.96577
     * status : false
     * alarm : []
     * creationDate : 2020-02-20 11:17:28
     * productCategory : 1
     * productCategoryMeaning : 农机
     */

    @SerializedName("deviceId")
    public int deviceId;
    @SerializedName("name")
    public String name;
    @SerializedName("productType")
    public String productType;
    @SerializedName("productBrand")
    public String productBrand;
    @SerializedName("productBrandMeaning")
    public String productBrandMeaning;
    @SerializedName("productModel")
    public String productModel;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("outDate")  //出厂日期
    public String outDate;
    @SerializedName("sn")
    public String sn;
    @SerializedName("code")  //出厂编号
    public String code;
    @SerializedName("lpn") //车牌号
    public String lpn;
    @SerializedName("jobType")
    public String jobType;
    @SerializedName("jobTypeMeaning")
    public String jobTypeMeaning;
    @SerializedName("tsn") //监控终端ID
    public String tsn;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("district")
    public String district;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
    @SerializedName("status")
    public boolean status;
    @SerializedName("creationDate") //添加时间
    public String creationDate;
    @SerializedName("productCategory")
    public String productCategory;
    @SerializedName("productCategoryMeaning")
    public String productCategoryMeaning;
    @SerializedName("propertyList")
    public List<PropertyListBean> propertyList;
    @SerializedName("alarm")
    public List<?> alarm;

    public static class PropertyListBean implements Serializable{
        /**
         * name : 标定功率（kw）
         * value : 37
         */

        @SerializedName("name")
        public String name;
        @SerializedName("value")
        public String value;
    }
}
