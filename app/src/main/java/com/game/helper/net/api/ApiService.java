package com.game.helper.net.api;

import com.game.helper.model.BannerResults;
import com.game.helper.model.BaseModel.HttpResultModel;
import com.game.helper.model.CashListResults;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.ConsumeListResults;
import com.game.helper.model.FriendRangeResultModel;
import com.game.helper.model.GeneralizeAccountInfoResultModel;
import com.game.helper.model.GeneralizeResults;
import com.game.helper.model.HotResults;
import com.game.helper.model.IncomeResultModel;
import com.game.helper.model.LoginResults;
import com.game.helper.model.LogoutResults;
import com.game.helper.model.MemberInfoResults;
import com.game.helper.model.NoticeResults;
import com.game.helper.model.ProfitListResults;
import com.game.helper.model.RecommendResults;
import com.game.helper.model.RegistResults;
import com.game.helper.model.ResetPasswdResults;
import com.game.helper.model.SpecialResults;
import com.game.helper.model.VerifyResults;
import com.game.helper.net.model.BannerRequestBody;
import com.game.helper.model.model.PayResultModel;
import com.game.helper.model.RechargeListResults;
import com.game.helper.net.model.BaseRequestBody;
import com.game.helper.net.model.FriendRangeRequestBody;
import com.game.helper.net.model.LoginRequestBody;
import com.game.helper.net.model.PayRequestBody;
import com.game.helper.net.model.RecommendRequestBody;
import com.game.helper.net.model.RegistRequestBody;
import com.game.helper.net.model.ResetPasswdRequestBody;
import com.game.helper.net.model.SinglePageRequestBody;
import com.game.helper.net.model.VerifyRequestBody;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {

    @POST("/member/login/")
    Flowable<HttpResultModel<LoginResults>> getApiLoginData(@Body LoginRequestBody baseRequestBody);

    @POST("/sys/get_banner_list/")
    Flowable<HttpResultModel<BannerResults>> getApiBannerData(@Body BannerRequestBody bannerRequestBody);

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

    //支付
    @POST("/G9game/paymentController.do?payment")
    Flowable<HttpResultModel<PayResultModel>> ApiPay(@Body PayRequestBody payRequestBody);

    //人脉排行榜
    @POST("/marketing/get_friend_rank/")
    Flowable<HttpResultModel<FriendRangeResultModel>> getFriendRank(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广收益排行榜
    @POST("/marketing/get_income_rank/")
    Flowable<HttpResultModel<FriendRangeResultModel>> getIncomeRank(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广收益列表
    @POST("/marketing/get_marketing_flow_list/")
    Flowable<HttpResultModel<IncomeResultModel>> getIncomeList(@Body FriendRangeRequestBody payRequestBody/*@Path("page") int pageNum*/);

    //推广账号信息
    @POST("/marketing/get_marketing_info/")
    Flowable<HttpResultModel<GeneralizeAccountInfoResultModel>> getGeneralizeAccountInfo();

    @POST("/member/reset_passwd/")//重置密码
    Flowable<HttpResultModel<ResetPasswdResults>> resetPassWord(@Body ResetPasswdRequestBody resetPasswdRequestBody);

    @POST("/account/get_consume_list/")//钱包-消费明细
    Flowable<HttpResultModel<ConsumeListResults>> getConsumeListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/account/get_recharge_list/")//钱包-充值明细
    Flowable<HttpResultModel<RechargeListResults>> getRechargeListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/marketing/get_marketing_reflect_list/")//钱包-提现明细
    Flowable<HttpResultModel<CashListResults>> getCashListData(@Body SinglePageRequestBody singlePageRequestBody);

    @POST("/marketing/get_marketing_flow_list/")//钱包-收益明细
    Flowable<HttpResultModel<ProfitListResults>> getProfitListData(@Body SinglePageRequestBody singlePageRequestBody);
}
