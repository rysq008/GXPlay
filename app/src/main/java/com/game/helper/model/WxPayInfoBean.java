package com.game.helper.model;

import java.io.Serializable;

public class WxPayInfoBean implements Serializable {

    private static final long serialVersionUID = 1698053816623451326L;
    private String appid;
    private String noncestr;
    /**
     * 关键字重复
     */
    private String packagestr;
    private String partnerid;
    private String prepayid;
    private String timestamp;
    private String sign;

    public WxPayInfoBean() {
        super();
    }

    public WxPayInfoBean(String appid, String noncestr, String packagestr,
                         String partnerid, String prepayid, String timestamp, String sign) {
        super();
        this.appid = appid;
        this.noncestr = noncestr;
        this.packagestr = packagestr;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackagestr() {
        return packagestr;
    }

    public void setPackagestr(String packagestr) {
        this.packagestr = packagestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WxPayInfoBean [appid=" + appid + ", noncestr=" + noncestr
                + ", packagestr=" + packagestr + ", partnerid=" + partnerid
                + ", prepayid=" + prepayid + ", timestamp=" + timestamp
                + ", sign=" + sign + "]";
    }

}
