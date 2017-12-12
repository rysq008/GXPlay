package com.game.helper.net.model;

import com.google.gson.annotations.SerializedName;

public class WeiXinPayRequestBody extends BaseRequestBody {

    public String sign;
    public String timestamp;
    public String noncestr;
    public String partnerid;
    public String prepayid;
    public String appid;
    public String return_code;
    @SerializedName("package")
    public String packagevalue;

    public WeiXinPayRequestBody(int page, String sign, String timestamp, String noncestr, String partnerid, String prepayid, String appid, String return_code, String packagevalue) {
        super(page);
        this.sign = sign;
        this.timestamp = timestamp;
        this.noncestr = noncestr;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.appid = appid;
        this.return_code = return_code;
        this.packagevalue = packagevalue;
    }

    public WeiXinPayRequestBody(int page) {
        super(1);
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getPackagevalue() {
        return packagevalue;
    }

    public void setPackagevalue(String packagevalue) {
        this.packagevalue = packagevalue;
    }
}
