package com.ikats.shop.model;


import java.io.Serializable;
import java.util.List;

public class ProvinceBean implements Serializable {

    /**
     * code : 110000
     * name : 北京市
     * cityList : [{"code":"110000","name":"北京市","areaList":[{"code":"110101","name":"东城区"},{"code":"110102","name":"西城区"},{"code":"110105","name":"朝阳区"},{"code":"110106","name":"丰台区"},{"code":"110107","name":"石景山区"},{"code":"110108","name":"海淀区"},{"code":"110109","name":"门头沟区"},{"code":"110111","name":"房山区"},{"code":"110112","name":"通州区"},{"code":"110113","name":"顺义区"},{"code":"110114","name":"昌平区"},{"code":"110115","name":"大兴区"},{"code":"110116","name":"怀柔区"},{"code":"110117","name":"平谷区"},{"code":"110118","name":"密云区"},{"code":"110119","name":"延庆区"}]}]
     */

    public String code;
    public String name;
    public List<CityListBean> cityList;


    public static class CityListBean {
        /**
         * code : 110000
         * name : 北京市
         * areaList : [{"code":"110101","name":"东城区"},{"code":"110102","name":"西城区"},{"code":"110105","name":"朝阳区"},{"code":"110106","name":"丰台区"},{"code":"110107","name":"石景山区"},{"code":"110108","name":"海淀区"},{"code":"110109","name":"门头沟区"},{"code":"110111","name":"房山区"},{"code":"110112","name":"通州区"},{"code":"110113","name":"顺义区"},{"code":"110114","name":"昌平区"},{"code":"110115","name":"大兴区"},{"code":"110116","name":"怀柔区"},{"code":"110117","name":"平谷区"},{"code":"110118","name":"密云区"},{"code":"110119","name":"延庆区"}]
         */

        public String code;
        public String name;
        public List<AreaListBean> areaList;

    }


    public static class AreaListBean {
        /**
         * code : 110101
         * name : 东城区
         */

        public String code;
        public String name;
    }
}
