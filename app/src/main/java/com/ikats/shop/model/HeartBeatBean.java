package com.ikats.shop.model;


import com.tamsiree.rxkit.RxDeviceTool;

import java.io.Serializable;

public class HeartBeatBean implements Serializable {
    /**
     * messageType : heartbreak
     * data : {"serialNo":"134563","posCode":"7E14F5878A27"}
     */

    public String messageType = "heartbreak";
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * serialNo : 134563
         * posCode : 7E14F5878A27
         */

        public String serialNo;
        public String posCode;

    }

    public static HeartBeatBean build(String serialNo) {
        HeartBeatBean beatBean = new HeartBeatBean();
        beatBean.data = new DataBean();
        beatBean.data.serialNo = serialNo;
        beatBean.data.posCode = RxDeviceTool.getMacAddress();
        return beatBean;
    }
}
