package com.ikats.shop.net.model;

public class PayinfoRequestBody extends BaseRequestBody {

    public String businessCode;// "PIB000513")
    public String tid;// outBizNo)
    static PayinfoRequestBody payinfoRequestBody = null;

    public PayinfoRequestBody(String businessCode, String tid) {
        super(1);
        this.businessCode = businessCode;
        this.tid = tid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        Builder() {
            payinfoRequestBody = null;
            payinfoRequestBody = new PayinfoRequestBody("", "");
        }

        public Builder setBusinessCode(String businessCode) {
            payinfoRequestBody.businessCode = businessCode;
            return this;
        }

        public Builder setTid(String tid) {
            payinfoRequestBody.tid = tid;
            return this;
        }

        public PayinfoRequestBody builder() {
            return payinfoRequestBody;
        }
    }

}
