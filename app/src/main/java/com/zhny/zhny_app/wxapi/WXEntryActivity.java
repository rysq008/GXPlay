package com.andorid.greenorange.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.andorid.greenorange.utils.ShareUtils;
import com.andorid.greenorange.views.ToastMgr;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;

import cn.droidlover.xdroidmvp.kit.Kits;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Gson mGson;
    private String WEIXIN_APP_ID;
    private String WEIXIN_APP_SECRET;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 微信事件回调接口注册
        if (TextUtils.isEmpty(WEIXIN_APP_ID)) {
            Toast.makeText(this, "app_id 不能为空", Toast.LENGTH_SHORT).show();
        }
        api = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, true);
        api.registerApp(WEIXIN_APP_ID);

        mGson = new Gson();
    }

    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + WEIXIN_APP_ID +
                "&secret=" + WEIXIN_APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        httpRequest(url, new ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }
            @Override
            public void onError(int errorCode, final String errorMsg) {

                showMessage("错误信息: " + errorMsg);
            }
            @Override
            public void onFailure(IOException e) {

                showMessage("登录失败");
            }
        });
    }

    private void showMessage(String str) {
        ToastMgr.showShortToast(str);
    }

    /**
     * 处理获取的授权信息结果
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        WXAccessTokenInfo tokenInfo = mGson.fromJson(response, WXAccessTokenInfo.class);
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息

            // 保存信息到手机本地
            ShareUtils.saveObject("wxtokeninfo",tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.access_token, tokenInfo.openid);
        } else {
            // 提示错误信息
            showMessage("错误信息: " + tokenInfo.errmsg);
        }
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }

    /**
     * 判断accesstoken是过期
     * @param accessToken token
     * @param openid 授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        httpRequest(url, new ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                if (validateSuccess(response)) {
                    // accessToken没有过期，获取用户信息
                    getUserInfo(accessToken, openid);
                } else {
                    // 过期了，使用refresh_token来刷新accesstoken
                    refreshAccessToken();
                }
            }
            @Override
            public void onError(int errorCode, final String errorMsg) {
                showMessage("错误信息: " + errorMsg);
            }
            @Override
            public void onFailure(IOException e) {
                showMessage("登录失败");
            }
        });
    }

    /**
     * 刷新获取新的access_token
     */
     private void refreshAccessToken() {
     // 从本地获取以存储的refresh_token
     final String refreshToken = ShareUtils.getObject("wxtokeninfo",WXAccessTokenInfo.class).refresh_token;
     if (TextUtils.isEmpty(refreshToken)) {
     return;
     }
     // 拼装刷新access_token的url请求地址
     String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
     "appid=" + WEIXIN_APP_ID +
     "&grant_type=refresh_token" +
     "&refresh_token=" + refreshToken;
     // 请求执行
     httpRequest(url, new ApiCallback<String>() {
    @Override
    public void onSuccess(String response) {
    // 判断是否获取成功，成功则去获取用户信息，否则提示失败
    processGetAccessTokenResult(response);
    }
    @Override
    public void onError(int errorCode, final String errorMsg) {
    showMessage("错误信息: " + errorMsg);
    // 重新请求授权
    loginWeixin(WXEntryActivity.this.getApplicationContext(), api);
    }
    @Override
    public void onFailure(IOException e) {
    showMessage("登录失败");
    // 重新请求授权
    loginWeixin(WXEntryActivity.this.getApplicationContext(), api);
    }
    });
     }

    /**
     * 获取用户信息
     *
     */
     private void getUserInfo(String access_token, String openid) {
     String url = "https://api.weixin.qq.com/sns/userinfo?" +
     "access_token=" + access_token +
     "&openid=" + openid;
     httpRequest(url, new ApiCallback<String>() {
    @Override
    public void onSuccess(String response) {
    // 解析获取的用户信息
    WXUserInfo userInfo = mGson.fromJson(response, WXUserInfo.class);
    ShareUtils.saveObject("wxuserinfo",userInfo);
  }
    @Override
    public void onError(int errorCode, String errorMsg) {
    showMessage("错误信息: " + errorMsg);
    }
    @Override
    public void onFailure(IOException e) {
    showMessage("获取用户信息失败");
    }
    });
     }
    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context.getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }
    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            // 发送成功
            case BaseResp.ErrCode.ERR_OK:
                // 获取code
                String code = ((SendAuth.Resp) resp).code;
                // 通过code获取授权口令access_token
                // 从手机本地获取存储的授权口令信息，判断是否存在access_token，不存在请求获取，存在就判断是否过期
                String accessToken = ShareUtils.getObject("wxtokeninfo",WXAccessTokenInfo.class).access_token;
                String openid = ShareUtils.getObject("wxtokeninfo",WXAccessTokenInfo.class).openid;
                if (!Kits.Empty.check(accessToken)) {
                    // 有access_token，判断是否过期有效
                    isExpireAccessToken(accessToken, openid);
                } else {
                    // 没有access_token
                    getAccessToken(code);
                }
                break;
        }
    }




    private OkHttpClient mHttpClient = new OkHttpClient.Builder().build();
    private Handler mCallbackHandler = new Handler(Looper.getMainLooper());
    /**
     * 通过Okhttp与微信通信
     * * @param url 请求地址
     * @throws Exception
     */
    public void httpRequest(String url, final ApiCallback<String> callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mCallbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 请求失败，主线程回调
                            callback.onFailure(e);
                        }
                    });
                }
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    if (!response.isSuccessful()) {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 请求出错，主线程回调
                                callback.onError(response.code(), response.message());
                            }
                        });
                    } else {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // 请求成功，主线程返回请求结果
                                    callback.onSuccess(response.body().string());
                                } catch (final IOException e) {
                                    // 异常出错，主线程回调
                                    mCallbackHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onFailure(e);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // Api通信回调接口
    public interface ApiCallback<T> {
        /**
         * 请求成功
         *
         * @param response 返回结果
         */
        void onSuccess(T response);
        /**
         * 请求出错
         *
         * @param errorCode 错误码
         * @param errorMsg 错误信息
         */
        void onError(int errorCode, String errorMsg);
        /**
         * 请求失败
         */
        void onFailure(IOException e);
    }
}