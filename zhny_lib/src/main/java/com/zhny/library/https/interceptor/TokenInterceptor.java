package com.zhny.library.https.interceptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.zhny.library.common.Constant;
import com.zhny.library.https.retrofit.vo.ErrorMessage;
import com.zhny.library.https.retrofit.vo.LibToken;
import com.zhny.library.presenter.login.custom.LoadingDialog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

/**
 * 添加token，待考虑token加密
 */
public class TokenInterceptor implements Interceptor {

    private static final String TOKEN_URL = Constant.Server.BASE_URL + "oauth/oauth/token";
    private static final String CONTENT = "grant_type=".concat(Constant.Server.GRANT_TYPE);

    private Context context;
    private LoadingDialog loadingDialog;

    public TokenInterceptor(Context context, LoadingDialog loadingDialog) {
        this.context = context;
        this.loadingDialog = loadingDialog;
    }

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.SP.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            String name = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.SP.LOGIN_USERNAME, "");
            String pwd = SPUtils.getInstance(Constant.SP.SP_NAME).getString(Constant.FINALVALUE.PASSWORD, "");
            String clientId = Constant.IS_LIB_TOKEN ? Constant.Server.CLIENT_MAP_ID : Constant.Server.CLIENT_ID;
            String secret = Constant.IS_LIB_TOKEN ? Constant.Server.CLIENT_MAP_SECRET : Constant.Server.CLIENT_SECRET;

            String actualUrl = CONTENT.concat("&username=").concat(name).concat("&password=").concat(pwd)
                    .concat("&client_id=").concat(clientId).concat("&client_secret=").concat(secret);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, actualUrl);
            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                String json = response.body().string();
                LibToken libTokenJson = JSON.parseObject(json, LibToken.class);
                token = Objects.requireNonNull(libTokenJson).accessToken;
                SPUtils.getInstance(Constant.SP.SP_NAME).put(Constant.SP.TOKEN, token);
            }
        }
        Request.Builder builder = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token);


        Response response = chain.proceed(builder.build());
        int responseCode = response.code();
        if (responseCode != 200) {
            try {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (context != null) {
                    assert response.body() != null;
                    BufferedSource source = response.body().source();
                    source.request(Long.MAX_VALUE);
                    String errorJson = source.getBuffer().clone().readString(Charset.forName("UTF-8"));
                    source.getBuffer().flush();
                    Log.d("【response error】", "json: " + errorJson);
                    ErrorMessage errorMessage = JSON.parseObject(errorJson, ErrorMessage.class);
                    final String toastMsg = getErrorMsg(errorMessage);
                    if (!TextUtils.isEmpty(toastMsg)) {
                        Single.just(toastMsg).observeOn(AndroidSchedulers.mainThread()).subscribe(s ->
                                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private String getErrorMsg(ErrorMessage errorMessage) {
        String msg = TextUtils.isEmpty(errorMessage.errorDescription) ? errorMessage.message : errorMessage.errorDescription;
        msg = TextUtils.isEmpty(msg) ? "" : msg;

        if (msg.contains("account.locked")) {
            msg = "密码错误次数过多，您的账户已被禁用！";
            if (Constant.IS_LIB_TOKEN) sendBroadCast();
        } else if (msg.contains("mapUser.notEnabled") || msg.contains("user.notActive")) {
            msg = "您的账号未激活，请联系管理员激活账号！";
            if (Constant.IS_LIB_TOKEN) sendBroadCast();
        } else if (msg.contains("username.notFound") || msg.contains("mapUser.notAllowed")) {
            msg = "用户名不存在！";
            if (Constant.IS_LIB_TOKEN) sendBroadCast();
        } else if (msg.contains("password.wrong")) {
            msg = "密码不正确！";
        } else {
            msg = TextUtils.isEmpty(msg) ? "请求失败！" : msg;
        }
        return msg;
    }

    //发送账号未激活的广播
    private void sendBroadCast() {
        if (context != null) {
            Intent intent = new Intent(Constant.Server.USER_NOT_ACTIVE);
            context.sendBroadcast(intent);
        }
    }


}