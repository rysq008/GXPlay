package com.zhny.library.https.api;

import com.zhny.library.base.BaseDto;
import com.zhny.library.presenter.login.model.dto.LoginDto;
import com.zhny.library.presenter.login.model.dto.TokenInfoDto;
import com.zhny.library.presenter.login.model.dto.UserInfoDto;
import com.zhny.library.presenter.login.model.vo.LoginVo;
import com.zhny.library.presenter.monitor.model.dto.LookUpVo;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * 登录api
 */
public interface LoginApiService {


    @POST("login")
    Flowable<BaseDto<LoginDto>> login(@Body LoginVo loginVo);

    @POST("oauth/oauth/token")
    Flowable<TokenInfoDto> getToken(@QueryMap Map<String, String> map);

    @GET("uaa/v1/users/querySelf")
    Flowable<UserInfoDto> getUserInfo();

    @GET("/fws/v1/organization/{organizationId}/lookup/findByCode/{code}")
    Flowable<BaseDto<LookUpVo>> queryFastCode(@Path("organizationId") String organizationId,
                                              @Path("code") String fastCode);
}
