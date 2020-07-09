package com.zhny.library.https.interceptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.common.Constant;
import com.zhny.library.https.retrofit.vo.LibToken;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * token 过期处理，待考虑token加密
 */
public class TokenAuthenticator implements Authenticator {

    private static final String TOKEN_URL = Constant.Server.BASE_URL + "oauth/oauth/token";
    private static final String CONTENT = "grant_type=".concat(Constant.Server.GRANT_TYPE);

    private Context context;

    public TokenAuthenticator(Context context) {
        this.context = context;
    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        Request originRequest = response.request();
        String name = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.SP.LOGIN_USERNAME, "");
        String pwd = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.PASSWORD, "");
        String clientId = Constant.IS_LIB_TOKEN ? Constant.Server.CLIENT_MAP_ID : Constant.Server.CLIENT_ID;
        String secret =  Constant.IS_LIB_TOKEN ? Constant.Server.CLIENT_MAP_SECRET: Constant.Server.CLIENT_SECRET;

        if (!TextUtils.isEmpty(originRequest.header("Authorization"))) {

            String actualUrl = CONTENT.concat("&username=").concat(name).concat("&password=").concat(pwd)
                    .concat("&client_id=").concat(clientId).concat("&client_secret=").concat(secret);

            SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.TOKEN, "");
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, actualUrl);
            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response tokenResponse = client.newCall(request).execute();
            if (tokenResponse.isSuccessful()) {
                assert tokenResponse.body() != null;
                String json = tokenResponse.body().string();
                LibToken libTokenJson = JSON.parseObject(json, LibToken.class);
                String token = Objects.requireNonNull(libTokenJson).accessToken;
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.TOKEN, token);
                return originRequest.newBuilder()
                        .header("Authorization","Bearer " + token)
                        .build();
            }
        }
        return null;
    }


}
























