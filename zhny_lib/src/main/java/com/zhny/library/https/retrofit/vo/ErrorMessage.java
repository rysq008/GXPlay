package com.zhny.library.https.retrofit.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by liming
 */
public class ErrorMessage implements Serializable {

    /**
     * error : unauthorized
     * error_description : user.notActive
     */
    @SerializedName("error")
    public String error;
    @SerializedName("error_description")
    public String errorDescription;
    @SerializedName("message")
    public String message;

}
