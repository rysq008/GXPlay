package com.game.helper.model.huanxin;

import com.game.helper.model.BaseModel.XBaseModel;

/**
 * Created by Tian on 2018/1/12.
 */

public class HanXinResponse<T> extends XBaseModel {

    /**
     * id : 3509
     * tenantId : 51593
     * greetingId : 259f14ad-673b-4067-9d9c-df8609d18961
     * greetingTextType : 0
     * greetingText : 您好,我是万万,很高兴为您服务,请您就关心的问题向我提问,如如何用积分充值话&quot;,您可能关心
     * createdTime : 1514985028000
     * lastUpdatedTime : 1515741252000
     * greetingJson : null
     */

    private int id;
    private int tenantId;
    private String greetingId;
    private int greetingTextType;
    private String greetingText;
    private long createdTime;
    private long lastUpdatedTime;
    private T greetingJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getGreetingId() {
        return greetingId;
    }

    public void setGreetingId(String greetingId) {
        this.greetingId = greetingId;
    }

    public int getGreetingTextType() {
        return greetingTextType;
    }

    public void setGreetingTextType(int greetingTextType) {
        this.greetingTextType = greetingTextType;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public T getGreetingJson() {
        return greetingJson;
    }

    public void setGreetingJson(T greetingJson) {
        this.greetingJson = greetingJson;
    }
}
