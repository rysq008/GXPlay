package com.zhny.library.presenter.monitor.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * created by liming
 */
public class LookUpVo implements Serializable {


    @SerializedName("lookupId")
    public int lookupId;
    @SerializedName("code")
    public String code;
    @SerializedName("description")
    public String description;
    @SerializedName("organizationId")
    public int organizationId;
    @SerializedName("disabled")
    public boolean disabled;
    @SerializedName("values")
    public List<ValuesBean> values;

    public static class ValuesBean implements Serializable{

        @SerializedName("lookupValueId")
        public int lookupValueId;
        @SerializedName("code")
        public String code;
        @SerializedName("lookupId")
        public int lookupId;
        @SerializedName("orderValue")
        public int orderValue;
        @SerializedName("description")
        public String description;

    }
}
