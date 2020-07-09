package com.zhny.library.https.retrofit.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * created by liming
 */
public class LibToken implements Serializable {


    /**
     * access_token : e6fba8d2-8ee7-4b9f-bb45-c17a1befd023
     * token_type : bearer
     * refresh_token : 6a51a32e-ca6d-4964-a9d0-d8cc084faa4a
     * expires_in : 43199
     * scope : default
     */

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("expires_in")
    public int expiresIn;

    @SerializedName("scope")
    public String scope;

}
