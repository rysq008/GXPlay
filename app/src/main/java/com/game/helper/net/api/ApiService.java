package com.game.helper.net.api;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.ResetPasswdRequestBody;
import com.game.helper.net.model.VerifyRequestBody;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiService {

    @POST("/member/login/")
    Flowable<HttpResultModel<LoginResults>> getApiLoginData(@Body LoginRequestBody baseRequestBody);

    @POST("/sys/get_banner_list/")
    Flowable<HttpResultModel<BannerResults>> getApiBannerData();

    //首页通告
    @POST("/sys/get_notification_list/")
    Flowable<HttpResultModel<NoticeResults>> getApiNoticeData();

    //首页专题
    //    @HTTP(method = "",path = "/user/delete",hasBody = true)
    @POST("/game/get_game_theme_list/")
    Flowable<HttpResultModel<SpecialResults>> getApiSpecialData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //首页热门推荐数据
    @POST("/game/get_recommend_game_list/")
    Flowable<HttpResultModel<HotResults>> getApiHotData(@Body BaseRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //首页自定义推荐数据
    @POST("/game/get_game_list/")
    Flowable<HttpResultModel<RecommendResults>> getApiRecommendData(@Body RecommendRequestBody baseRequestBody/*@Path("type") String type, @Path("number") int pageSize, @Path("page") int pageNum*/);

    //经典分类列表
    @POST("/game/get_class_type_list/")
    Flowable<HttpResultModel<ClassicalResults>> getApiClassicalData();

    //普通分类列表
    @POST("/game/get_type_list/")
    Flowable<HttpResultModel<CommonResults>> getApiCommonData();

    //推广帐号信息
    @POST("/marketing/get_marketing_info/")
    Flowable<HttpResultModel<GeneralizeResults>> getApiGeneralizeAccountData();

    @POST("/member/register/")//注册
    Flowable<HttpResultModel<RegistResults>> ApiRegitst(@Body RegistRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("market_num") String market_num*/);

    @POST("/member/login/")//登陆
    Flowable<HttpResultModel<LoginResults>> ApiLogin(@Body LoginRequestBody baseRequestBody/*@Path("phone") String phone, @Path("code") String code, @Path("type") String type, @Path("channel_num") String channel_num*/);

    @POST("/member/logout/")//登出
    Flowable<HttpResultModel<LogoutResults>> ApiLogout();

    @POST("/member/info/")//会员信息
    Flowable<HttpResultModel<MemberInfoResults>> getApiMemberInfo();

    @POST("/public/get_tel_verify/")//验证码
    Flowable<HttpResultModel<VerifyResults>> getApiVerify(@Body VerifyRequestBody verifyRequestBody/*@Path("phone") String phone, @Path("usefor") String usefor*/);

    @POST("/member/reset_passwd/")//重置密码
    Flowable<HttpResultModel<ResetPasswdResults>> resetPassWord(@Body ResetPasswdRequestBody resetPasswdRequestBody);
}
