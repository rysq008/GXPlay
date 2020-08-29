package com.ikats.shop.net.api;

import com.ikats.shop.model.BaseModel.HttpResultModel;
import com.ikats.shop.model.LoginBean;
import com.ikats.shop.model.LogoutBean;
import com.ikats.shop.model.RegisterBean;
import com.ikats.shop.net.model.BaseRequestBody;
import com.ikats.shop.net.model.LoginRequestBody;
import com.ikats.shop.net.model.RegistRequestBody;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    //注册
    @POST("app/member/register/")
    Flowable<HttpResultModel<RegisterBean>> Regitst(@Body RegistRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("market_num") String market_num*/);

    //登录
    @POST("app/member/login/")
    Flowable<HttpResultModel<LoginBean>> Login(@Body LoginRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("types") String types, @Path("channel_num") String channel_num*/);

    //登出
    @POST("app/member/logout/")
    Flowable<HttpResultModel<LogoutBean>> Logout();

    //登出
    @GET("/api/findTeacherByMobile/{mobile}")
    Flowable<HttpResultModel> getCode(@Path(value = "mobile", encoded = true) String mobile);

    //上传文件(方式一)
    @POST("{url}")
    Flowable<HttpResultModel<Object>> uploadApiFile(@Path(value = "url", encoded = true) String url, @Body RequestBody Body);

    //上传文件(方式二)
    @Multipart
    @POST("{url}")
    Flowable<HttpResultModel<String>> uploadApiFile(@Path(value = "url", encoded = true) String url, @Part MultipartBody.Part file);


    //==========================================三方平台服务器地址接口========================================
    @GET("/sns/oauth2/access_token")
    Flowable<Object> getUserWeixinInfo(@QueryMap Map<String, String> params);

    //登录
    @POST("/mw/app/login")
    Flowable<HttpResultModel<LoginBean>> getApiLoginData(@Body LoginRequestBody baseRequestBody);
    //==========================================三方平台服务器地址接口========================================

    @GET
    Flowable<ResponseBody> getData(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Flowable<ResponseBody> postData(@Url String url, @FieldMap Map<String, Object> params);

    @POST
    Flowable<ResponseBody> postData(@Url String url, @Body Object body);

    @POST
    Flowable<ResponseBody> postData(@Url String url, @Body RequestBody body);

    @Streaming
    @GET
    Flowable<ResponseBody> downloadData(@Url String url);


    enum HttpMethod {
        GET,
        POST,
        POST_BODY,
        POST_JSON,//retrfix自动BaseRequestBody将转化成json
        UPLOAD,
        DOWNLOAD,
        OTHER
    }
}
